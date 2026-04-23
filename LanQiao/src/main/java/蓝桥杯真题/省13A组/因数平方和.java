package 蓝桥杯真题.省13A组;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.省13A组
 * @Project：LanQiaoBei
 * @name：因数平方和
 * @Date：2025/4/2 18:06
 * @Filename：因数平方和
 */
public class 因数平方和 {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        long n = scanner.nextLong();
        long l = gFunction(n);
        System.out.println(l);
    }

    static long mod=1000000007;
    public static long gFunction(Long n){
        long ans=0;
        for (Long l = 1l; l <= n; l++) {
            ans+=fFunction(l);
            ans%=mod;
        }
        //ans++;
        return ans;
    }

    public static long fFunction(Long n){
        Long ans=0l;
        HashSet<Long> set=new HashSet<>();
        for (Long l = 1l; l *l<= n; l++) {
            if (n%l==0){
                set.add(l);
                set.add(n/l);
            }
        }
        for (Long aLong : set) {
            ans+=aLong*aLong;
            //ans%=mod;
        }
        //ans++;
        return ans;
    }


}
