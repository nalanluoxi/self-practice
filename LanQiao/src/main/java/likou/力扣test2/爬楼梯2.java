package likou.力扣test2;

import 稀土掘金.倒排索引;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：爬楼梯2
 * @Date：2025/7/1 19:37
 * @Filename：爬楼梯2
 */
public class 爬楼梯2 {

    public static void main(String[] args) {
        System.out.println(climbStairs(3));
    }
    public static int climbStairs(int n) {
        if (n<=2){
            return n;
        }
        int []dp=new int[n];
        dp[0]=1;
        dp[1]=2;
        for (int i =2 ; i < n; i++) {
            dp[i]=dp[i-1]+dp[i-2];
        }
        return dp[n-1];
    }
}
