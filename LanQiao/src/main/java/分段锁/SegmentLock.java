package 分段锁;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author 纳兰洛熙
 * @Package：分段锁
 * @Project：LanQiaoBei
 * @name：SegmentLock
 * @Date：2025/5/11 16:33
 * @Filename：SegmentLock
 */
public class SegmentLock {

    private int allCount;
    private int segmentCount;

    private double expansion=2/3;

    private final String lockKey;

    private List<Lock> locks;
    private Map<String,Lock> locking;
    private List<Lock> unlock;


    public SegmentLock(int allCount, int segmentCount, String lockKey) {
        this.allCount = allCount;
        this.segmentCount = segmentCount;
        this.lockKey = lockKey;
        this.locks = new LinkedList<>();
        this.locking = new HashMap<>();
        this.unlock = new LinkedList<>();
    }

    public void initlock(){
        int temp = allCount / segmentCount;
        int lasttemp = allCount % segmentCount;
        for (int i = 0; i < segmentCount; i++) {
            Lock lock = new Lock();
            lock.setKey(lockKey+":segment"+i);
            lock.setValue(temp);
        }
    }

    private class Lock{
        String key;
        int value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public Lock() {
        }

        public Lock(String key, int value) {
            this.key = key;
            this.value = value;
        }
    }
}
