package luogu;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class 麦森数 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();
        BigInteger mod = new BigInteger("10");
        mod=mod.pow(500);
        BigInteger base=new BigInteger("2");
        //BigInteger res = base.modPow(BigInteger.valueOf(m),base).subtract(BigInteger.valueOf(1));
        //BigInteger res = base.pow(m).subtract(BigInteger.valueOf(1));
        BigInteger res = base.pow(m).mod(mod).subtract(BigInteger.valueOf(1));
        String string = res.toString();
        System.out.println((int)(m*Math.log10(2)+1));
        if (string.length()>500){
            string=string.substring(string.length()-500);
            for (int i = 0; i < 10; i++) {
                System.out.println(string.substring(i*50,i*50+50));
            }
        }else {
            for (int j=0,b=1;j<500-string.length();j++){//小于500时
                System.out.print("0");//先输出补位0的数目
                if (b%50==0){
                    System.out.println();
                }
                b++;
            }
            int p=(500-string.length())%50;//计算还差多少到达50字符
            for (int k=0,a=p;k<string.length();k++){
                System.out.print(string.charAt(k));//输出
                if (k==49-p){
                    System.out.println();//达到50位时换行
                    a=0;//令a为0 重新计算
                }
                if (a!=0 && a%50==0){
                    System.out.println();//每50字符换行
                }
                a++;
            }

        }


    }

    private static long pow(long n, long m) {
        long res = 1;
        long base = n;
        // ArrayList<Long> result=new ArrayList<>();
        long mod10 = 1000000009;
        m = m % mod10;
        while (m != 0) {
            if ((m % 2) == 1) {
                res = (res * base) % mod10;
            }
            base = (base * base) % mod10;
            m = m / 2;

        }
        return res;
    }
}
