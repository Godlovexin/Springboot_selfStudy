package com.chinaunicom.sun.frame.service;

import org.springframework.stereotype.Component;

@Component
public interface redisService {
    void setRedisKeyString(String key,String value);
    String getRedisKeyString(String key);
}
