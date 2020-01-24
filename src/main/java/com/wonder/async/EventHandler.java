package com.wonder.async;

import java.util.List;

/**
 * @Author: wonder
 * @Date: 2020/1/14
 */
public interface EventHandler {
    /**
     * 执行事件的方法
     * @param model
     */
    void doHandle(EventModel model);

    /**
     * 定义支持的事件类型
     * @return
     */
    List<EventType> getSupportEventTypes();
}
