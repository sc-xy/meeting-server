package com.example.meeting.model.entity;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import lombok.NoArgsConstructor;

/**
 * @author sc-xy
 * @time 2025/3/30
 */
@Data
public class MeetingInfo {
    /**
     * 会议号
     */
    private String meetingId;

    /**
     * 会议人员
     */
    private Set<User> users;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        private String userId;

        private String username;
    }
}
