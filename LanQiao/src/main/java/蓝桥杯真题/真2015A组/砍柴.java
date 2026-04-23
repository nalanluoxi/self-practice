package 蓝桥杯真题.真2015A组;

import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.真2015A组
 * @Project：LanQiaoBei
 * @name：砍柴
 * @Date：2025/3/24 21:52
 * @Filename：砍柴
 */
public class 砍柴 {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();
        int[] nums=new int[n];
        for (int i = 0; i < n; i++) {
            int num = scan.nextInt();
            nums[i]=num;
        }
        int [] dp=new int[1000001];
        dp[0]=0;
        dp[1]=0;
        dp[2]=1;
        dp[3]=1;
        int [ ]zhishu=new int[100000];
        int index=0;
        for (int i = 0; i < 100000; i++) {
            if (isZhiShu(i)){
                zhishu[index]=i;
                index++;
            }
        }
        for (int i = 4; i < dp.length; i++) {
            int flag=0;
            for (int j = 0; j < index && zhishu[j]<=i; j++) {
                if (dp[i-zhishu[j]]==0){
                    flag=1;
                    dp[i]=1;
                    break;
                }
            }
            if (flag==0){
                dp[i]=0;
            }
        }
        for (int num : nums) {
            System.out.println(dp[num]);
        }

    }

    public static boolean isZhiShu(Integer num) {
        if (num == 0 || num == 1) {
            return false;
        }
        for (int i = 2; i * i <= num; i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }
}
