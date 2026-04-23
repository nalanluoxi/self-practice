package 蓝桥杯真题.真2015A组;

import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.真2015A组
 * @Project：LanQiaoBei
 * @name：回文字符串
 * @Date：2025/3/25 9:35
 * @Filename：回文字符串
 */
public class 回文字符串 {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String string = scanner.nextLine();
            huiWen(string);
        }
    }

    public static void huiWen(String s){
        if (isHuiWen(s)){
            System.out.println("Yes");
            return;
        }
        int len = s.length();
        for (int i = 1; i < len; i++) {
            String substring = s.substring(0, i);
            if (isHuiWen(substring)){
                String substring1 = s.substring(i, len);
                if (isOnlylqb(substring1)){
                    System.out.println("Yes");
                    return;
                }
            }
        }
        System.out.println("No");

    }
    public static boolean isOnlylqb(String s){
        int len = s.length();
        for (int i = 0; i < len; i++) {
            if (s.charAt(i)!='l' && s.charAt(i)!='q' && s.charAt(i)!='b'){
                return false;
            }
        }
        return true;
    }
    public static boolean isHuiWen(String s){
        StringBuffer sb=new StringBuffer(s);
        String s1 = sb.reverse().toString();
        if (s1.equals(s)){
            return true;
        }
        return false;
    }
}
