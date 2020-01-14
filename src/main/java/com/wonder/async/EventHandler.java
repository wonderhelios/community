package com.wonder.async;

import java.util.List;

/**
 * @Author: wonder
 * @Date: 2020/1/14
 */
public interface EventHandler {
    void doHandle(EventModel model);
    List<EventType> getSupportEventTypes();
}
