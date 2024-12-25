package com.example.btl_android.util;

import java.util.HashMap;
import java.util.Map;

public class RateLimiter {
    private final int maxRequests;
    private final long timeWindowMillis;
    private final Map<String, UserRequestInfo> userRequests = new HashMap<>();

    public RateLimiter(int maxRequests, long timeWindowMillis) {
        this.maxRequests = maxRequests;
        this.timeWindowMillis = timeWindowMillis;
    }

    public synchronized boolean isAllowed(String userId) {
        long currentTime = System.currentTimeMillis();
        UserRequestInfo requestInfo = userRequests.getOrDefault(userId, new UserRequestInfo(0, currentTime));

        if (currentTime - requestInfo.startTime > timeWindowMillis) {
            requestInfo.startTime = currentTime;
            requestInfo.requestCount = 0;
        }

        if (requestInfo.requestCount < maxRequests) {
            requestInfo.requestCount++;
            userRequests.put(userId, requestInfo);
            return true;
        } else {
            return false;
        }
    }

    private static class UserRequestInfo {
        int requestCount;
        long startTime;

        UserRequestInfo(int requestCount, long startTime) {
            this.requestCount = requestCount;
            this.startTime = startTime;
        }
    }
}