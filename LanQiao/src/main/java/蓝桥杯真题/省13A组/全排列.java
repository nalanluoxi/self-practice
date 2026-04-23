package 蓝桥杯真题.省13A组;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.省13A组
 * @Project：LanQiaoBei
 * @name：全排列
 * @Date：2025/4/2 17:42
 * @Filename：全排列
 */
public class 全排列 {
    public static void main(String[] args) {
       /* Scanner scanner=new Scanner(System.in);
        int i = scanner.nextInt();
        init(i);
        System.out.println(allSum);*/
       /* while (true){
            help();
        }*/
        while (true){
            String string = sc.nextLine();
            long n = Long.valueOf( string);
            init(n);
        }

    }

    private static int MOD = 998244353;
    static Scanner sc = new Scanner(System.in);

    public static void help(){
        String string = sc.nextLine();
        long n = Long.valueOf( string);
        long ans = n*(n-1)/2%MOD;
        for (int i = 3;i<=n;i++){
            ans = ans*i%MOD;
        }
        System.out.println(ans);

    }

    static long mod= 998244353l;
    static List<Long> num;

    static int allSum;
    public static void init(long n){
        num=new ArrayList<>();
        allSum=0;
        for (long i = 0l; i < n; i++) {
            num.add(i+1);
        }
        dp(0);
    }

    public static void dp(int index){
        if (index==num.size()-1){
            //System.out.println(num);
            allSum+=getSum(num);
            allSum%=mod;
            System.out.println(allSum);
            return;
        }
        int size = num.size();
        for (int i = index; i < size; i++) {
            swap(index,i);
            dp(index+1);
            swap(index,i);
        }
    }


    public static int getSum(List<Long> list){
        int ans=0;
        for (int i = 0; i < list.size(); i++) {
            Long now = list.get(i);
            for (int j = 0; j <i; j++) {
                Long befor = list.get(j);
                if (befor<now){
                    ans++;
                }
            }
        }

        return ans;
    }



    public static void swap(int i,int j){
        Long temp = num.get(i);
        num.set(i,num.get(j));
        num.set(j,temp);
    }

}
