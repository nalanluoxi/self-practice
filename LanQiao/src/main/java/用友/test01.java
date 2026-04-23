package 用友;

import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：用友
 * @Project：LanQiaoBei
 * @name：test01
 * @Date：2025/8/18 19:15
 * @Filename：test01
 */
public class test01 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] strings = scanner.nextLine().split(",");
        int[]nums=new int[strings.length];
        String string = scanner.nextLine();
        int n=Integer.valueOf(string);
        for (int i = 0; i < strings.length; i++) {
            nums[i]=Integer.valueOf(strings[i]);
        }
        System.out.println(help(nums,n));

    }

    public static boolean help(int[] nums,int n){
        int count=0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i]==1){
                continue;
            }
            if (nums[i]==0){
                if (i!=0 &&nums[i-1]!=1){
                    count++;
                    nums[i]=1;
                }
            }
        }

        return count>=n;
    }
}
