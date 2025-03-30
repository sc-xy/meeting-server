package com.example.meeting.model.entity;

import lombok.Data;

import java.util.List;

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
    private List<Long> userIds;
}
