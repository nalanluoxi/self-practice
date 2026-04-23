package likou;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：基本计算器
 * @Date：2025/5/29 11:07
 * @Filename：基本计算器
 */
public class 基本计算器 {

    public static void main(String[] args) {
        String s = "5-(6+8)";
        System.out.println(calculate(s));
    }

    public static int calculate(String s) {
        int ans = 0;
        Deque<Integer> deque = new LinkedList<>();
        deque.push(1);
        int sign = 1;
        int l = s.length();
        int i=0;
        while (i<l){
            System.out.println(s.charAt(i));
            if (s.charAt(i)==' '){
                i++;
            } else if (s.charAt(i)=='+') {
                sign=deque.peek();
                i++;
            }else if (s.charAt(i)=='-') {
                sign=-deque.peek();
                i++;
            }else if (s.charAt(i)=='(') {
                deque.push(sign);
                i++;
            }else if (s.charAt(i)==')') {
                deque.pop();
                i++;
            }else {
                long num=0;
                while (i<l&&Character.isDigit(s.charAt(i))){
                    num=num*10+s.charAt(i)-'0';
                    i++;
                }
                System.out.println("num:"+num);
                ans+=sign*num;
                System.out.println("ans:"+ans);
            }
        }
        return ans;
    }


}
