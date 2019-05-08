package com.chinaunicom.sun.frame;

import com.chinaunicom.sun.frame.service.redisService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FrameApplicationTests {


	@Autowired
	private  redisService redisService;


	@Test
	public void contextLoads() {

		//redisService.setRedisKeyString("sun","sqx",100);
		//System.out.println(redisService.getRedisKeyString("name"));

		String uuid = UUID.randomUUID().toString();
		System.out.println(redisService.getLock("sun",uuid,10));

		//redisService.setRedisKeyString("sun","sunqx",100);
		//redisService.releaseLock("name",uuid);




	}

	volatile int i = 0;
	AtomicInteger atomicInteger = new AtomicInteger();
	@Test
	public void testThread(){
		for (int j = 0; j < 20; j++) {
			new Thread(()->{
				for (int k = 0; k <= 1000; k++) {
					i++;
					atomicInteger.getAndIncrement();
				}
			},String.valueOf(i)).start();

		}

		if(Thread.activeCount() > 2){
			Thread.yield();
		}
		System.out.println(i);
		System.out.println("AtomicInteger的值："+atomicInteger);

	}

	private static final Log log = LogFactory.getLog(FrameApplicationTests.class);
	/*请求总数*/
	public  static int clientTotal = 1000;
	/*同时并发执行的线程数*/
	public  static int threadTotal = 200;
	public volatile static int count = 0;

	@Test
	public  void testRedis() throws Exception{
		ExecutorService executorService = Executors.newCachedThreadPool();
		final Semaphore semaphore = new Semaphore(threadTotal);
		final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);

		redisService.setRedisKey("pronum",200,10000000);


		for (int i = 0; i < clientTotal; i++) {
			executorService.execute(()->{
				try{
					semaphore.acquire();//获取一个许可

					redisService.seckill("MSKEY");//执行锁

					semaphore.release();//释放申请的一个许可
				}catch (Exception e){
					log.error("exception",e);
				}
				countDownLatch.countDown();
			});
		}
		countDownLatch.await();
		executorService.shutdown();

		System.out.println("并发执行完，剩余库存:" + redisService.getRedisKeyString("pronum"));

		log.info("count:"+count);
	}


}
