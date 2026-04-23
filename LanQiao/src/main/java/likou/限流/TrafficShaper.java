package likou.限流;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 流量打散限流器 - 基于用户层级的动态限流
 * 使用说明：
 * 1. 创建实例：TrafficShaper shaper = new TrafficShaper();
 * 2. 在每次请求前调用：boolean allow = shaper.shouldAllow(userId);
 * 3. 如果返回true，则处理请求；否则拒绝请求
 */
public class TrafficShaper {
    // 用户层级定义
    private enum UserLevel {
        TINY,      // 极少用户 (≤1,000)
        SMALL,     // 少量用户 (1,000 ~ 10,000,000)
        MEDIUM,    // 中级用户 (10,000,000 ~ 1,000,000,000)
        LARGE      // 大用户 (>1,000,000,000)
    }

    // 用户状态记录
    private static class UserState {
        final AtomicLong totalRequests = new AtomicLong(0);  // 历史总请求数
        final AtomicInteger windowCount = new AtomicInteger(0); // 当前窗口计数
        volatile UserLevel level;  // 当前用户层级
        volatile long lastUpdateTime = System.currentTimeMillis(); // 最后更新时间
    }

    // 配置参数
    private final ConcurrentHashMap<String, UserState> userStates = new ConcurrentHashMap<>();
    private final ReentrantLock globalLock = new ReentrantLock();
    private final long windowSizeMs = 1000; // 时间窗口大小(毫秒)

    // 层级限流阈值配置 (每秒允许的请求数)
    private static final int TINY_LIMIT = 1000;   // 极少用户
    private static final int SMALL_LIMIT = 100;   // 少量用户
    private static final int MEDIUM_LIMIT = 10;    // 中级用户
    private static final int LARGE_LIMIT = 1;      // 大用户

    /**
     * 判断是否允许当前请求
     * @param userId 用户ID
     * @return true表示允许请求，false表示需要限流
     */
    public boolean shouldAllow(String userId) {
        // 获取或创建用户状态
        UserState state = userStates.computeIfAbsent(userId, k -> new UserState());

        // 检查时间窗口是否需要重置
        long currentTime = System.currentTimeMillis();
        if (currentTime - state.lastUpdateTime > windowSizeMs) {
            resetWindow(state, currentTime);
        }

        // 更新用户层级（如果需要）
        updateUserLevel(state);

        // 获取当前层级的限流阈值
        int limit = getLimitForLevel(state.level);

        // 检查是否超过限流阈值
        if (state.windowCount.get() >= limit) {
            return false; // 限流
        }

        // 更新计数
        state.windowCount.incrementAndGet();
        state.totalRequests.incrementAndGet();
        return true;
    }

    /**
     * 重置时间窗口计数
     */
    private void resetWindow(UserState state, long currentTime) {
        state.windowCount.set(0);
        state.lastUpdateTime = currentTime;
    }

    /**
     * 更新用户层级
     */
    private void updateUserLevel(UserState state) {
        long total = state.totalRequests.get();
        UserLevel newLevel;

        if (total <= 1000) {
            newLevel = UserLevel.TINY;
        } else if (total <= 10_000_000) {
            newLevel = UserLevel.SMALL;
        } else if (total <= 1_000_000_000) {
            newLevel = UserLevel.MEDIUM;
        } else {
            newLevel = UserLevel.LARGE;
        }

        // 只有当层级变化时才更新
        if (state.level != newLevel) {
            state.level = newLevel;
            // 层级切换时重置窗口计数，避免突然的限流变化
            state.windowCount.set(0);
        }
    }

    /**
     * 获取当前层级的限流阈值
     */
    private int getLimitForLevel(UserLevel level) {
        if (level == null) return TINY_LIMIT; // 默认值

        switch (level) {
            case TINY: return TINY_LIMIT;
            case SMALL: return SMALL_LIMIT;
            case MEDIUM: return MEDIUM_LIMIT;
            case LARGE: return LARGE_LIMIT;
            default: return TINY_LIMIT;
        }
    }

    /**
     * 清理长时间未活动的用户（可选，防止内存泄漏）
     * @param inactiveThresholdMs 不活动时间阈值（毫秒）
     */
    public void cleanupInactiveUsers(long inactiveThresholdMs) {
        long now = System.currentTimeMillis();
        userStates.entrySet().removeIf(entry ->
                now - entry.getValue().lastUpdateTime > inactiveThresholdMs
        );
    }

    /**
     * 获取当前用户状态（测试用）
     */
    public String getUserStatus(String userId) {
        UserState state = userStates.get(userId);
        if (state == null) return "用户不存在";
        return String.format("用户: %s, 层级: %s, 总请求: %d, 当前窗口: %d/%d",
                userId, state.level, state.totalRequests.get(),
                state.windowCount.get(), getLimitForLevel(state.level));
    }

    // 测试用例
    public static void main(String[] args) throws InterruptedException {
        TrafficShaper shaper = new TrafficShaper();
        String tinyUser = "tiny_user";
        String largeUser = "large_user";

        // 模拟极少用户请求
        for (int i = 0; i < 5; i++) {
            System.out.println("极少用户请求 " + i + ": " +
                    shaper.shouldAllow(tinyUser) + " | " +
                    shaper.getUserStatus(tinyUser));
        }

        // 模拟大用户请求
        // 首先设置大用户的历史请求量
        UserState largeState = shaper.userStates.computeIfAbsent(largeUser, k -> new UserState());
        largeState.totalRequests.set(1_000_000_001L);
        largeState.level = UserLevel.LARGE;

        for (int i = 0; i < 5; i++) {
            System.out.println("大用户请求 " + i + ": " +
                    shaper.shouldAllow(largeUser) + " | " +
                    shaper.getUserStatus(largeUser));
            Thread.sleep(250); // 模拟请求间隔
        }

        // 清理不活跃用户
        shaper.cleanupInactiveUsers(300_000); // 5分钟不活跃
    }
}