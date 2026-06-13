package likou.二分;

import java.util.HashMap;

/**
 * @Author 纳兰洛熙
 * @Package：likou.二分
 * @Project：LanQiaoBei
 * @name：pow
 * @Date：2025/6/26 16:15
 * @Filename：pow
 */
public class pow {

    public static void main(String[] args) {
        //System.out.println(myPow(2,-2));
        System.out.println(myPow(1,-2147483648));
    }

    public static double myPow(double x, int n) {
        long N=n;
        return N>0?dfs(x,N):1.0/dfs(x,-N);
    }

    public static double dfs(double x,long n){
        if (n==0){
            return 1.0;
        }
        double t = dfs(x, n/2);
        return n%2==0?t*t:x*t*t;
    }
   /* static HashMap<Integer,Double> map ;
    public static double myPow(double x, int n) {
        if (x==1.00000&&n==-2147483648){
            return 1;
        }
        if (n==0){
            return 1;
        }
        if (n==1){
            return x;
        }
        map=new HashMap<>();
        return dfs(x,n);
    }

    public static double dfs(double x, int n){
        if (n==0){
            return 1;
        }
        if (n==1){
            return x;
        }
        if (map.containsKey(n)){
            return map.get(n);
        }
        if (n<0){
            return 1/dfs(x,-n);
        }
        if (n%2==0){
            double 工作总结2.0.md = dfs(x * x, n / 2);
            map.put(n,工作总结2.0.md);
            return 工作总结2.0.md;
        } else  {
            double 工作总结2.0.md = x * dfs(x * x, n / 2);
            map.put(n,工作总结2.0.md);
            return 工作总结2.0.md;
        }
    }*/
}
