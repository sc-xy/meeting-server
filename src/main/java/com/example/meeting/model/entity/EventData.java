package com.example.meeting.model.entity;

import lombok.Data;

import java.util.Map;

@Data
public class EventData {

    /**
     * 事件类型
     */
    private String eventName;

    /**
     * 数据
     */
    private Map<String, Object> data;
}
