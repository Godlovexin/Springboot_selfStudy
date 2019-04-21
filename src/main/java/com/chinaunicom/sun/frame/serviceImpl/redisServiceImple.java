package com.chinaunicom.sun.frame.serviceImpl;

import com.chinaunicom.sun.frame.service.redisService;
import com.chinaunicom.sun.frame.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName $ {Name}
 * @Description TODO
 * @Author $ {USER}
 * @Date 2019/4/21 16:34
 * @Version 1.0
 **/
@Service
public class redisServiceImple implements redisService {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void setRedisKeyString(String key, String value) {
        redisUtil.set(key,value);
    }

    @Override
    public String getRedisKeyString(String key) {
        return redisUtil.get(key).toString();
    }
}
