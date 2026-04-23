package likou.动态规划;


import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：大盗阿福
 * @Date：2025/5/22 21:41
 * @Filename：大盗阿福
 */
public class 大盗阿福 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        String string1 = sc.nextLine();
        int n = Integer.parseInt(string1);
        for (int i = 0; i < n; i++) {
            sc.nextLine();
            String string = sc.nextLine();
            adFu(string);
        }
    }

    public static void adFu(String str){
        String[] split = str.split(" ");
        int [] arr=new int[split.length];
        for (int i = 0; i < split.length; i++) {
            arr[i]=Integer.parseInt(split[i]);
        }
        int length = arr.length;
        int[]dp=new int[length];
        int ans=0;
        dp[length-1]=arr[length-1];
        for (int i = length-2; i >=0; i--) {
            
            ans=Math.max(ans,dp[i]);
        }
        System.out.println(ans);
    }
}
