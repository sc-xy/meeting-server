package com.example.meeting.model.entity;

import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.Session;

/**
 * OnlineInfo 存储在线用户的相关信息
 *
 * @author : rogeryxu
 **/
public class OnlineInfo {

    /**
     * 会议信息列表
     */
    public static ConcurrentHashMap<String, MeetingInfo> meetingMap = new ConcurrentHashMap<>();

    /**
     * 在线用户信息列表
     */
    public static ConcurrentHashMap<String, UserInfo> userMap = new ConcurrentHashMap<>();

    /**
     * 在线用户session列表
     */
    public static ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<>();
}
