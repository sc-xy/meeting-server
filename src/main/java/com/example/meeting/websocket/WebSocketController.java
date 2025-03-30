package com.example.meeting.websocket;

import com.example.meeting.model.entity.EventData;
import com.example.meeting.model.entity.MeetingInfo;
import com.example.meeting.model.entity.UserInfo;
import com.example.meeting.utils.JsonUtils;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sc-xy
 * @time 2025/3/30
 */
@ServerEndpoint("/websocket/{userId}")
@Component
@Slf4j
public class WebSocketController {
    /**
     * 连接的用户id
     */
    private Long userId;

    /**
     * 在线用户信息列表
     */
    private static ConcurrentHashMap<Long, UserInfo> userMap = new ConcurrentHashMap<>();

    /**
     * 用户上线
     *
     * @param session
     * @param userIdStr
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userIdStr) {
        // 1. 获取用户信息
        this.userId = Long.parseLong(userIdStr);
        UserInfo userInfo = userMap.get(userId);

        // 2. 这里重复的情况先不处理
        if (Objects.isNull(userInfo)) {
            userInfo = new UserInfo(userId, session);
        }

        // 3. 加入在线列表
        userMap.put(userId, userInfo);

        // 4. 连接成功，返回个人信息
        EventData send = new EventData();
        send.setEventName("__success");
        Map<String, Object> map = new HashMap<>();
        map.put("userID", userId);
        send.setData(map);
        session.getAsyncRemote().sendText(JsonUtils.getInstance().toJson(send));
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
            userMap.remove(userId);
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
}
