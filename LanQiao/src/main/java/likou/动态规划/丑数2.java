package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：丑数2
 * @Date：2025/4/8 18:53
 * @Filename：丑数2
 */
public class 丑数2 {
    public static void main(String[] args) {
        System.out.println(nthUglyNumber(10));
    }

    static long[] dp;

    public static int nthUglyNumber(int n) {
        dp = new long[1691];
        dp[1] = 1;
        int p2 = 1, p3 = 1, p5 = 1;
        for (int i = 2; i <= n; i++) {
            long a = dp[p2] * 2;
            long b = dp[p3] * 3;
            long c = dp[p5] * 5;
            long cur = Math.min(a, Math.min(b, c));
            if (cur == a) {
                p2++;
            }
            if (cur == b) {
                p3++;
            }
            if (cur == c) {
                p5++;
            }
            dp[i] = cur;
        }
        return (int)dp[n];
    }

    /* *//*static long[] dp;*//*
    static PriorityQueue<Long> queue;
    static Set<Long> set;

    static long[] list;
    public static int nthUglyNumber(int n) {
        queue = new PriorityQueue<>();
        list=new long[]{2,3,5};
        set=new HashSet<>();
        queue.add(1L);
        set.add(1L);
        long poll=0;
        for (int i = 0; i < n; i++) {
            poll = queue.poll();
            for (long l : list) {
                long newnum = poll * l;
                if (set.add(newnum)){
                    queue.offer(newnum);
                }
            }
        }
        return (int)poll;
    }
*/
  /*  public static boolean isUgly(int n) {
        if (n<=0){
            return false;
        }
        int [] list=new int[]{2,3,5};
        for (int i : list) {
            while (n%i==0){
                n/=i;
            }
        }
        return n==1;
    }*/
}
