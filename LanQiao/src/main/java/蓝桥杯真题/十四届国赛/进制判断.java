package 蓝桥杯真题.十四届国赛;

import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.十四届国赛
 * @Project：LanQiaoBei
 * @name：进制判断
 * @Date：2025/6/14 19:40
 * @Filename：进制判断
 */
public class 进制判断 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        Integer n = Integer.valueOf(sc.nextLine());
        for (Integer i = 0; i < n; i++) {
            String[] split = sc.nextLine().split(" ");
            judge(split[0],split[1]);
        }
    }

    public static void judge(String a,String b){
        int nb=Integer.valueOf(b);
        int n1 = getN(a, 2);
        if (n1<=nb){
            System.out.println(n1);
            return;
        }
        int n2 = getN(a, 4);
        if (n2<=nb){
            System.out.println(n2);
            return;
        }
        int n3 = getN(a, 8);
        if (n3<=nb){
            System.out.println(n3);
            return;
        }
        int n4 = getN(a, 16);
        if (n4<=nb){
            System.out.println(n4);
        }
        System.out.println(-1);
        return;
    }

    public static int getN(String a,int n){
        int ans=0;
        for (int i = a.length()-1,j=0; i >= 0; i--,j++) {
            char c = a.charAt(i);
            int t=0;
            if(Character.isDigit(c)){
                t=c-'0';
            }else if(!Character.isDigit(c)&&n==16){
                t=c-'A'+10;
            }else {
                return Integer.MAX_VALUE;
            }
            ans+=Math.pow(n,j)*t;
        }
        return ans;
    }
}
