package 蓝桥杯真题.决12A组;

import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.决12A组
 * @Project：LanQiaoBei
 * @name：二级制问题
 * @Date：2025/4/11 21:47
 * @Filename：二级制问题
 */
public class 二级制问题 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int k = scanner.nextInt();
        int count=0;
        for (int i = 1; i <= n; i++) {
            String er = getEr(i);
            if (isTrue(er,k)){
                //System.out.println(i);
                count++;
            }
        }
        System.out.println(count);

    }
    public static boolean isTrue(String str,int k){
        String replace = str.replace("0", "");
        return replace.length()==k;
    }

    public static String getEr(long num){
        String str="";
        while (num>0){
            long now=num%2;
            num/=2;
            str=now+str;
        }
        //System.out.println(str);
        return str;
    }
}
