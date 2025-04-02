package com.example.meeting.controller;

import static com.example.meeting.model.entity.OnlineInfo.meetingMap;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.example.meeting.common.BaseResponse;
import com.example.meeting.common.ErrorCode;
import com.example.meeting.common.ResultUtils;
import com.example.meeting.model.dto.meeting.CreateMeetingRequest;
import com.example.meeting.model.dto.meeting.QueryMeetingRequest;
import com.example.meeting.model.entity.MeetingInfo;
import com.example.meeting.utils.MeetingCodeUtils;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sc-xy
 * @time 2025/3/30
 */
@Slf4j
@RestController
@RequestMapping("/meeting")
public class MeetingController {

    /**
     * 创建新会议
     *
     * @return
     */
    @PostMapping("/create")
    public BaseResponse<MeetingInfo> createMeeting(@RequestBody CreateMeetingRequest req) {
        // 1. 生成不存在的会议号
        String meetingId = MeetingCodeUtils.generateMeetingCode();

        while (meetingMap.containsKey(meetingId)) {
            meetingId = MeetingCodeUtils.generateMeetingCode();
        }

        // 2. 创建会议信息
        MeetingInfo meetingInfo = new MeetingInfo();
        meetingInfo.setMeetingId(meetingId);
        meetingInfo.setUsers(new ConcurrentHashSet<>());

        meetingMap.put(meetingId, meetingInfo);

        return ResultUtils.success(meetingInfo);
    }

    /**
     * 查询会议信息
     *
     * @param req
     * @return
     */
    @PostMapping("/query")
    public BaseResponse<MeetingInfo> getMeeting(@RequestBody QueryMeetingRequest req) {
        MeetingInfo meetingInfo = meetingMap.get(req.getMeetingId());

        if (Objects.isNull(meetingInfo)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "会议不存在");
        }

        return ResultUtils.success(meetingInfo);
    }
}
