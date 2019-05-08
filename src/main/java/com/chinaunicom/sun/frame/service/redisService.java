package com.chinaunicom.sun.frame.service;

import org.springframework.stereotype.Component;

@Component
public interface redisService {
    void setRedisKey(String key,Object value,long time);
    String getRedisKeyString(String key);
    boolean getLock(String lockKey, String value, int expireTime);
    boolean releaseLock(String lockKey, String value);
    boolean seckill( String key);
}
