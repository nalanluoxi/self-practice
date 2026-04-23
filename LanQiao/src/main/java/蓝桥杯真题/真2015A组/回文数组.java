package 蓝桥杯真题.真2015A组;

import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.真2015A组
 * @Project：LanQiaoBei
 * @name：回文数组
 * @Date：2025/3/24 20:50
 * @Filename：回文数组
 */
public class 回文数组 {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();
        long[] nums=new long[n];
        for (int i = 0; i < n; i++) {
            nums[i]=scan.nextLong();
        }
        int ans=0;
        for (int i = 0; i < n/2; i++) {
            if (nums[i]==nums[n-i-1]){
                continue;
            } else if (nums[i]>nums[n-i-1]) {
                long tax1 = nums[i] - nums[n - i - 1];
             //   long tax2 = nums[i + 1] - nums[i - i - 2];
                nums[i]-=tax1;
                if (nums[i+1]>nums[n-i-2]){
                    nums[i+1]-=tax1;
                }else {
                    nums[i+1]+=tax1;
                }
                ans+=tax1;
            } else if (nums[i]<nums[n-i-1]) {
                long tax1 = nums[n - i - 1] - nums[i];
                nums[i]+=tax1;
                if (nums[i+1]>nums[n-i-2]){
                    nums[i+1]-=tax1;
                }else {
                    nums[i+1]+=tax1;
                }
                ans+=tax1;
            }
        }
        System.out.println(ans);

        scan.close();
    }


}
