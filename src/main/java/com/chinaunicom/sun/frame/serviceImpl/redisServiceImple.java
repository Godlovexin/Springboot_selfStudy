package com.chinaunicom.sun.frame.serviceImpl;

import com.chinaunicom.sun.frame.service.redisService;
import com.chinaunicom.sun.frame.utils.RedisLock;
import com.chinaunicom.sun.frame.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TransferQueue;

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
    @Autowired
    private RedisLock redisLock;

    @Override
    public void setRedisKey(String key, Object value,long time) {
        redisUtil.set(key,value,time);
    }


    @Override
    public String getRedisKeyString(String key) {
        return redisUtil.get(key).toString();
    }


    @Override
    public boolean getLock(String lockKey, String value, int expireTime) {
        return redisUtil.getLock(lockKey, value, expireTime);
    }

    @Override
    public boolean releaseLock(String lockKey, String value) {
        return redisUtil.releaseLock(lockKey,value);
    }

    /**
     * 锁超时时间，防止线程在入锁以后，无限的执行等待
     */
    private int expireMsecs = 60 * 1000;

    /***
     * 抢购代码
     * @param key pronum 首先用客户端设置数量
     * @return
     */
    public  boolean  seckill( String key) {
        RedisLock lock = new RedisLock(redisLock.getRedisTemplate(), key, 10000, 20000);

        try {
            if (lock.lock()) {
                System.out.println("======" + Thread.currentThread().getName() + "进程加锁成功！======");
                // 需要加锁的代码
                String pronum=lock.get("pronum");

                //修改库存
                if(Integer.parseInt(pronum)-1>=0) {
                    lock.set("pronum",String.valueOf(Integer.parseInt(pronum)-1));
                    System.out.println("库存数量:"+(Integer.parseInt(pronum)-1)+"  成功!!!"+Thread.currentThread().getName());
                }else {
                    System.out.println("手慢拍大腿");
                }

                return true;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 为了让分布式锁的算法更稳键些，持有锁的客户端在解锁之前应该再检查一次自己的锁是否已经超时，再去做DEL操作，因为可能客户端因为某个耗时的操作而挂起，
            // 操作完的时候锁因为超时已经被别人获得，这时就不必解锁了。 ————这里没有做

            lock.unlock();

        }
        return false;
    }

}
