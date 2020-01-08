package com.wonder.service;

import com.wonder.controller.QuestionController;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 利用字典树做敏感词过滤
 *
 * @Author: wonder
 * @Date: 2020/1/8
 */
@Service
public class SensitiveService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);
    /**
     * 默认的敏感词替换值
     */
    private static final String DEFAULT_REPLECEMENT = "**";

    @Override
    public void afterPropertiesSet() throws Exception {
        /**
         * 读取敏感词文件
         */
        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("SensitiveWords.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                lineTxt = lineTxt.trim();
                addWord(lineTxt);
            }
            bufferedReader.close();
        } catch (Exception e) {
            logger.error("读取敏感词文件出错:" + e.getMessage());
        }
    }

    /**
     * 字典树节点
     */
    private class TrieNode {
        /**
         * true:为敏感词的结尾,false:不为敏感词结尾
         */
        private boolean end = false;
        /**
         * key:下一个字符,value:对应的结点
         */
        Map<Character, TrieNode> subNodes = new HashMap<>();

        /**
         * 添加结点
         */
        void addSubNode(Character key, TrieNode node) {
            subNodes.put(key, node);
        }

        /**
         * 获取下一个结点
         */
        TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        /**
         * 判断是否为敏感词结尾
         */
        boolean isKeyWordEnd() {
            return end;
        }

        /**
         * 设置敏感词结尾
         */
        void setKeyWordEnd(boolean end) {
            this.end = end;
        }

        /**
         * 获取子结点个数
         */
        public int getSubNodeCount() {
            return subNodes.size();
        }
    }

    /**
     * 根结点
     */
    private TrieNode rootNode = new TrieNode();

    /**
     * 过滤符号
     */
    private boolean isSymbol(char c) {
        int ic = (int) c;
        //0x2E80-0x9FFF:为东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    /**
     * 添加敏感词
     */
    private void addWord(String lineText) {
        TrieNode tempNode = rootNode;
        //循环遍历每个字符
        for (int i = 0; i < lineText.length(); i++) {
            Character c = lineText.charAt(i);
            //过滤符号
            if (isSymbol(c)) {
                continue;
            }
            TrieNode node = tempNode.getSubNode(c);
            if (node == null) {
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }
            tempNode = node;
            if (i == lineText.length() - 1){
                //关键字结束,设置结束标志
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    /**
     * 过滤关键字
     * @param text 待过滤文本
     * @return  过滤后的文本
     */
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return text;
        }
        StringBuilder res = new StringBuilder();

        TrieNode tempNode = rootNode;
        //回滚数
        int begin = 0;
        //当前比较结点
        int position = 0;
        while (position < text.length()){
            char c = text.charAt(position);
            /**
             * 若为符号则直接跳过
             */
            if(isSymbol(c)){
                if(tempNode == rootNode){
                    res.append(c);
                    begin++;
                }
                position++;
                continue;
            }
            tempNode = tempNode.getSubNode(c);
            //如果当前位置匹配结束
            if(tempNode == null){
                //以begin为开头的字符串不存在敏感词
                res.append(text.charAt(begin));
                //跳跃到下一个字符开始测试
                position=begin+1;
                begin=position;
                //字典树回到根节点
                tempNode = rootNode;
            }else if(tempNode.isKeyWordEnd()){
                //若发现敏感字
                res.append(DEFAULT_REPLECEMENT);
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            }else{
                position++;
            }
        }
        res.append(text.substring(begin));
        return res.toString();
    }
}
