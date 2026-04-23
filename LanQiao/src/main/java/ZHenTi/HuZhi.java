package ZHenTi;

import java.util.Map;
import java.util.Scanner;

public class HuZhi {
    public static void main(String[] args) {
        Scanner scan=new Scanner(System.in);
        long a=scan.nextLong();
        long b=scan.nextLong();
        huzhi(a,b);
        scan.close();
    }

    static  int  mod =998244353;


    public static void huzhi(long a,long b){

        if (a==1){
            System.out.println("0");
            return;
        }

        long ans=a;
        long res=a;
        for (int i=2;i< Math.sqrt(a);i++){
            if (ans%i==0){
                while (ans%i==0){
                    ans=ans/i;
                }
                res=(res%mod)-(res%mod)/(i%mod);
                res%=mod;
            }
        }
        if (ans>1){
            res=res-res/ans;
        }

    }

    /*public static long fastmi(long a,long b){
        long res=1;
        a%=mod;
        while (b!=0){
            if (b%2==1){
                res=(res*a)%mod;
            }
            a=(a*a)%mod;
            b/=2;
        }
    }*/
}
