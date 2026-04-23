package 蓝桥杯真题.决12A组;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.决12A组
 * @Project：LanQiaoBei
 * @name：纯质数
 * @Date：2025/4/11 20:07
 * @Filename：纯质数
 */
public class 纯质数 {
    public static void main(String[] args) {
        init();
        help();
        System.out.println("1903");
    }

    static List<Integer> zhishu;
    static List<Integer> tans;
    public static void help2(){
        zhishu=new ArrayList<>();
        zhishu.add(2);
        zhishu.add(3);
        zhishu.add(5);
        zhishu.add(7);
        tans=new ArrayList<>();
        int bignum=20210605;
        for (int i = 2; i < bignum; i++) {
            if (isZhi(i)){
                int temp=1;
                int in=0;
                boolean flag=true;
                while (temp!=0){
                    in=temp%10;
                    temp=temp/10;
                    if (!zhishu.contains(in)){
                        flag=false;
                        break;
                    }
                }
                if (flag){
                    tans.add(i);
                    System.out.println(i);
                }
            }
        }
        System.out.println(tans.size());
    }


    static List<Integer> ans;
    public static void help(){
        ans=new ArrayList<>();
        for (Integer num : list) {
            addNums(num);
        }
        System.out.println("ans.size: " +ans.size());
    }

    public static boolean addNums(int n){
        if (n==0||n==1){
            return false;
        }
        int i;
        int temp=n;
        while (n!=0){
            i = n % 10;
            n=n/10;
            if (!list.contains(i)){
                return false;
            }
        }
        System.out.println(temp);
        ans.add(temp);
        return true;
    }

    static List<Integer> list ;
    public static void init() {
        list=new ArrayList<>();
        int bignum = 20210605;
        for (int i = 2; i < bignum; i++) {
            if (isZhi(i)){
                list.add(i);

                //System.out.println(i);
            }
        }
       // System.out.println(list);
        System.out.println("list size:"+list.size());
        System.out.println("1283133");
    }

    public static boolean isZhi(int n){
        if (n==1){
            return false;
        }
        for (int i = 2; i *i<= n; i++) {
            if (n%i==0){
                return false;
            }
        }
        return true;
    }
}
