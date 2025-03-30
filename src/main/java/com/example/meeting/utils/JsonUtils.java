package com.example.meeting.utils;

import com.google.gson.Gson;

/**
 * @author sc-xy
 * @time 2025/3/30
 */
public class JsonUtils {
    private static final Gson gson = new Gson();

    public static Gson getInstance() {
        return gson;
    }
}
