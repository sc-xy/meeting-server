package com.example.meeting.websocket;

import static com.example.meeting.model.entity.OnlineInfo.meetingMap;
import static com.example.meeting.model.entity.OnlineInfo.userMap;

import com.example.meeting.model.entity.EventData;
import com.example.meeting.model.entity.MeetingInfo;
import com.example.meeting.model.entity.MeetingInfo.User;
import com.example.meeting.model.entity.UserInfo;
import com.example.meeting.utils.JsonUtils;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author sc-xy
 * @time 2025/3/30
 */
@ServerEndpoint("/websocket/{userId}/{username}")
@Component
@Slf4j
public class WebSocketServer {
    /**
     * 连接的用户id
     */
    private Long userId;

    /**
     * 用户上线
     *
     * @param session
     * @param userIdStr
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userIdStr, @PathParam("username") String username) {
        // 1. 获取用户信息
        this.userId = Long.parseLong(userIdStr);

        UserInfo userInfo = userMap.get(userId);

        // 2. 这里重复的情况先不处理
        if (Objects.isNull(userInfo)) {
            userInfo = new UserInfo(userId, username, session);
        }

        // 3. 加入在线列表
        userMap.put(userIdStr, userInfo);

        log.info(userId + "-->onOpen......");
    }

    /**
     * 用户下线
     */
    @OnClose
    public void onClose() {
        log.info(userId + "-->onClose......");
        UserInfo userInfo = userMap.get(userId);
        if (Objects.nonNull(userInfo)) {
            userMap.remove(userId.toString());
            try {
                userInfo.getSession().close();
            } catch (IOException e) {
                log.error("error when close session with userId: " + userId, e);
            }
        }
    }

    /**
     * 接收消息
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("receive data:" + message);
        handleMessage(message, session);
    }

    /**
     * 处理消息
     *
     * @param message
     * @param session
     */
    private void handleMessage(String message, Session session) {
        EventData data;
        try {
            data = JsonUtils.getInstance().fromJson(message, EventData.class);
        } catch (JsonSyntaxException e) {
            System.out.println("json解析错误：" + message);
            return;
        }
        switch (data.getEventName()) {
            case "JOIN_ROOM": {
                joinRoom(data.getData());
                break;
            }
            case "OFFER": {
                handleOffer(data.getData());
                break;
            }
            case "ANSWER": {
                handleAnswer(data.getData());
                break;
            }
            case "ICE_CANDIDATE": {
                handleIceCandidate(data.getData());
                break;
            }
            case "CHAT_MESSAGE": {
                handleChatMessage(data.getData());
                break;
            }
            default:
                break;
        }

    }

    /**
     * 发生错误时
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("error when connect with userId: " + userId, error);
    }

    /**
     * 加入房间，返回房间信息
     *
     * @param data
     */
    public void joinRoom(Map<String, Object> data) {
        String meetingId = data.get("meetingId").toString();
        String userId = data.get("userId").toString();
        UserInfo userInfo = userMap.get(userId);

        MeetingInfo meetingInfo = meetingMap.get(meetingId);
        if (Objects.nonNull(meetingInfo)) {
            // 加入房间
            meetingInfo.getUsers().add(new User(userId, userInfo.getUsername()));

            // 组装房间信息
            EventData send = new EventData();
            send.setEventName("JOIN_ROOM_SUCCESS");
            Map<String, Object> map = new HashMap<>();
            map.put("meetingId", meetingId);
            map.put("users", meetingInfo.getUsers());
            send.setData(map);

            // 返回给用户
            sendMessage(userId, send);
        }
    }

    /**
     * 处理offer消息
     *
     * @param data
     */
    public void handleOffer(Map<String, Object> data) {
        String userId = data.get("userId").toString();
        String targetUserId = data.get("targetUserId").toString();

        log.info("offer userId: {} targetUserId: {} data: {}", userId, targetUserId, data);

        UserInfo user = userMap.get(userId);
        data.put("username", user.getUsername());

        EventData send = new EventData();
        send.setEventName("OFFER");
        send.setData(data);
        sendMessage(targetUserId, send);
    }

    /**
     * 处理answer消息
     *
     * @param data
     */
    public void handleAnswer(Map<String, Object> data) {
        String userId = data.get("userId").toString();
        String targetUserId = data.get("targetUserId").toString();

        log.info("answer userId: {} targetUserId: {} data: {}", userId, targetUserId, data);

        EventData send = new EventData();
        send.setEventName("ANSWER");
        send.setData(data);
        sendMessage(targetUserId, send);
    }

    /**
     * 处理iceCandidate消息
     *
     * @param data
     */
    public void handleIceCandidate(Map<String, Object> data) {
        String userId = data.get("userId").toString();
        String targetUserId = data.get("targetUserId").toString();

        log.info("iceCandidate userId: {} targetUserId: {} data: {}", userId, targetUserId, data);

        EventData send = new EventData();
        send.setEventName("ICE_CANDIDATE");
        send.setData(data);
        sendMessage(targetUserId, send);
    }

    /**
     * 处理聊天消息
     *
     * @param data
     */
    private void handleChatMessage(Map<String, Object> data) {
        String userId = data.get("userId").toString();
        String username = data.get("username").toString();
        String meetingId = data.get("meetingId").toString();
        String message = data.get("message").toString();

        log.info("chat message userId: {} meetingId: {} message: {}", userId, meetingId, message);

        EventData send = new EventData();
        send.setEventName("CHAT_MESSAGE");
        send.setData(data);
        for (User user : meetingMap.get(meetingId).getUsers()) {
            if (!user.getUserId().equals(userId)) {
                sendMessage(user.getUserId(), send);
            }
        }
    }

    /**
     * 发送消息给指定用户
     *
     * @param userId
     * @param data
     */
    private void sendMessage(String userId, EventData data) {
        UserInfo userInfo = userMap.get(userId);
        if (Objects.nonNull(userInfo)) {
            try {
                userInfo.getSession().getBasicRemote().sendText(JsonUtils.getInstance().toJson(data));
                log.info("send data to userId: " + userId + " msg: " + data);
            } catch (IOException e) {
                log.error("error when send message to userId: {} msg: {}", userId, data, e);
            }
        }
    }
}
