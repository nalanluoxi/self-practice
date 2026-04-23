package 蓝桥杯真题.省12A组;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.省12A组
 * @Project：LanQiaoBei
 * @name：货物摆放
 * @Date：2025/4/10 21:54
 * @Filename：货物摆放
 */
public class 货物摆放 {


    public static void main(String[] args) {
        List<Long> list=new ArrayList<>();
        long n=2021041820210418l;
        for (long i=1;i*i<=n;i++){
            if (n%i==0){
                list.add(i);
                if(n/i!=i){
                    list.add(n/i);
                }
            }
        }
        System.out.println(list.size());
        long ans=0;
        for (Long x : list) {
            for (Long y : list) {
                for (Long z : list) {
                    if (x*y*z==n){
                        ans++;
                         System.out.println(x+" "+y+" "+z);
                    }
                }
            }
        }
        System.out.println(ans);

        System.out.println("2430");
    }

    /* public static void main(String[] args) {

        tans = new ArrayList<>();
        set = new HashSet<>();
        //long n = 4;
         long n=2021041820210418l;
        help(n, 1);
        //System.out.println(set.size());
        System.out.println(ans);
    }

    static List<Long> tans;
    static Set<List<Long>> set;
    static long ans=0;

    public static void help(long n, long sum) {
        if (tans.size()>3||sum > n) {
            return;
        }
        if (sum == n &&tans.size()<=3) {
            if (set.add(new ArrayList<>(tans))){
                if (tans.size()==3){
                    ans++;
                } else if (tans.size()==2) {
                    ans+=3;
                } else if (tans.size()==1) {
                    ans+=3;
                }
            }
            System.out.println(tans.toString());
            return;
        }
        for (long i = n; i > 1; i--) {
            if (sum * i > n) {
                continue;
            }
            tans.add(i);
            help(n, sum * i);
            tans.remove(tans.size() - 1);
        }
    }*/
}
