package com.wonder.util;

import com.sun.mail.util.MailSSLSocketFactory;
import com.sun.mail.util.MimeUtil;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: wonder
 * @Date: 2020/1/16
 */
@Service
public class MailSender implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private static Properties properties;

    @Autowired
    private VelocityEngine  velocityEngine;

    public boolean sendWithHTMLTemplate(String to, String subject,
                                        String template, Map<String,Object> model){
        String from = "976563760@qq.com";

        try{
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable","true");
            properties.put("mail.smtp.ssl.socketFactory",sf);

            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("976563760@qq.com","sujvbodxevjpbbeg");
                }
            });
            MimeMessage message = new MimeMessage(session);

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
            mimeMessageHelper.setFrom(new InternetAddress(from));
            mimeMessageHelper.setTo(new InternetAddress(to));
            mimeMessageHelper.setSubject(subject);

            String result = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
                    template,"UTF-8",model);
            mimeMessageHelper.setText(result,true);

            Transport.send(message);
        }catch (Exception e){
            logger.info("发送邮件失败:",e);
            return false;
        }
        return true;
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        properties = System.getProperties();
        properties.setProperty("mail.smtp.host","smtp.qq.com");
        properties.setProperty("mail.smtp.auth","true");
    }
}
