package RedisLock;

import java.util.concurrent.TimeUnit;

/**
 * @Author 纳兰洛熙
 * @Package：RedisLock
 * @Project：LanQiaoBei
 * @name：RedissionLock
 * @Date：2025/7/9 20:21
 * @Filename：RedissionLock
 */
public class RedissionLock {

    public long lastTime;
    public String lockName;



    //释放锁
    public boolean unlock(){

        return true;
    }
    //直接返回
    public boolean tryLock(int last, TimeUnit unit){


        return true;
    }

    //阻塞等待
    public boolean  lock(int last, TimeUnit unit){

        return true;
    }

}
