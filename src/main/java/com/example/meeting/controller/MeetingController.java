package com.example.meeting.controller;

import com.example.meeting.common.BaseResponse;
import com.example.meeting.common.ResultUtils;
import com.example.meeting.model.dto.meeting.CreateMeetingRequest;
import com.example.meeting.model.entity.MeetingInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sc-xy
 * @time 2025/3/30
 */
@Slf4j
@RestController
@RequestMapping("/meeting")
public class MeetingController {

    /**
     * 会议信息列表
     */
    private static ConcurrentHashMap<String, MeetingInfo> meetingMap = new ConcurrentHashMap<>();

    /**
     * 创建新会议
     *
     * @return
     */
    @PostMapping("/create")
    public BaseResponse<String> createMeeting(CreateMeetingRequest req) {
        return ResultUtils.success("success");
    }
}
