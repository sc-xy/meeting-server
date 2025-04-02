package com.example.meeting.utils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * MeetingCodeUtils
 *
 * @author : rogeryxu
 **/
public class MeetingCodeUtils {

    /**
     * 允许存在的字符
     */
    private static final String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * 会议号长度
     */
    private static final int CODE_LENGTH = 6;

    public static String generateMeetingCode() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        char[] code = new char[CODE_LENGTH];

        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            code[i] = ALLOWED_CHARACTERS.charAt(randomIndex);
        }

        return new String(code);
    }

}
