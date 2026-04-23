package 蓝桥杯真题.真2015A组;

import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.真2015A组
 * @Project：LanQiaoBei
 * @name：数字诗意
 * @Date：2025/3/24 20:42
 * @Filename：数字诗意
 */
public class 数字诗意 {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();
        long ans=0l;
        for (int i = 0; i < n; i++) {
            int now = scan.nextInt();
            while (now%2==0 && now>1){
                now=now/2;
            }
            if (now==1){
                ans++;
            }
        }
        System.out.println(ans);
        scan.close();
    }

    public boolean isShiYi(int a){


        return false;
    }
}
