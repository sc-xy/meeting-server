package com.example.meeting.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.websocket.Session;
import lombok.Setter;

/**
 * @author sc-xy
 * @time 2025/3/30
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 会议号
     */
    private String meetingId;
}
