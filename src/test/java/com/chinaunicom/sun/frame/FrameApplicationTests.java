package com.chinaunicom.sun.frame;

import com.chinaunicom.sun.frame.service.redisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FrameApplicationTests {


	@Autowired
	private  redisService redisService;


	@Test
	public void contextLoads() {

		redisService.setRedisKeyString("name","sqx");
		System.out.println(redisService.getRedisKeyString("name"));

	}

}
