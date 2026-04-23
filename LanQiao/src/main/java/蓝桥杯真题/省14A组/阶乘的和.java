package 蓝桥杯真题.省14A组;

import java.util.HashMap;
import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.省14A组
 * @Project：LanQiaoBei
 * @name：阶乘的和
 * @Date：2025/3/27 21:27
 * @Filename：阶乘的和
 */
public class 阶乘的和 {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] split = scanner.nextLine().split(" ");
        long[] nums=new long[n];
        for (int i = 0; i < n; i++) {
            nums[i]=Long.valueOf(split[i]);
        }
        help(nums);
    }

    public static void help(long [] nums){
        jiecheng=new HashMap<>();
        jiecheng.put(1L,1L);
        jiecheng.put(2L,2L);
        jiecheng.put(3L,6L);
        long sum=0;
        for (int i = 0; i < nums.length; i++) {
            sum+=jie(nums[i]);
        }
        long ans=0;
        for (long l = 1; l <=sum-1; l++) {
            if (isYing(jie(l),sum)){
                ans=l;
            }
        }
        System.out.println(ans);
        return;
    }

    public static boolean isYing(long num1,long num2){
        if (num1==1||num2==1||num1==num2){
            return true;
        }
        return num2%num1==0;
    }

    static HashMap<Long,Long> jiecheng;
    public static long jie(long num){
        if (num==1){
            return 1;
        }
        if (jiecheng.containsKey(num)){
            return jiecheng.get(num);
        }
        long l = num * jie(num - 1);
        jiecheng.put(num,l);
        return l;
    }
}
