package 用友;

import java.sql.SQLOutput;
import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：用友
 * @Project：LanQiaoBei
 * @name：test03
 * @Date：2025/8/18 19:36
 * @Filename：test03
 */
public class test03 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String[] split = in.nextLine().split(" ");
        Integer n = Integer.valueOf(split[0]);
        Integer tar = Integer.valueOf(split[1]);
        String[] strings = in.nextLine().split(" ");
        int[]nums=new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            nums[i]=Integer.valueOf(strings[i]);
        }


        help(nums,n,tar);

        /*help(new int[]{100,200,150,300,250},5,400);*/
    }

    public static void help(int[]nums,int n,int tar){
       int[]dp=new int[n];
       int ans=Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            dp[i]=nums[i];
            if (i-2>=0){
                dp[i]+=dp[i-2];
            }
            if (dp[i]>=tar){
                ans=Math.min(ans,i/2+1);
                for (int j = 0; j < i; j++) {
                    if (dp[i]-dp[j]>=tar){
                        ans=Math.min(ans,(i-j)/2+1);
                    }
                }

            }
        }
        System.out.println(ans);
    }
}
