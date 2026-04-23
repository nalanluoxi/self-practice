package example.java2.MyThreadPoolTest;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2.MyThreadPollTest
 * @Project：LanQiaoBei
 * @name：RejectedHandler
 * @Date：2025/4/21 16:52
 * @Filename：RejectedHandler
 */
public interface RejectedHandler {
    void rejected(Runnable runnable, MyThreadPool myThreadPoll);
}
