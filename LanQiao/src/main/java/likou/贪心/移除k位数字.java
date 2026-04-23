package likou.贪心;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou.贪心
 * @Project：LanQiaoBei
 * @name：移除k位数字
 * @Date：2025/6/10 11:06
 * @Filename：移除k位数字
 */
public class 移除k位数字 {
    public static void main(String[] args) {
       // System.out.println(removeKdigits("1432219",3));
        //System.out.println(removeKdigits("33526221184202197273",19));
       // System.out.println(removeKdigits("112",1));
        System.out.println(removeKdigits("10200",1));
        System.out.println(removeKdigits1("10200",1));
    }

    public static String removeKdigits1(String num, int k) {
        Deque<Integer> deque=new LinkedList<>();
        int len=num.length();
        for(int i=0;i<len;i++){
            int temp=num.charAt(i)-'0';
            while(!deque.isEmpty()&&k>0&&deque.peekLast()>temp){
                deque.pollLast();
                k--;
            }
            deque.offerLast(temp);
        }
        for(int i=0;i<k;i++){
            deque.pollLast();
        }
        StringBuilder sb=new StringBuilder();
        while(!deque.isEmpty()){
            int t=deque.pollFirst();
            if(t==0&&sb.length()==0){
                continue;
            }else{
                sb.append(t);
            }
        }
        return sb.length()==0?"0":sb.toString();
    }
    public static String removeKdigits(String num, int k) {
        Deque<Integer>deque=new LinkedList<>();
        for (int i = 0; i < num.length(); i++) {
            int temp = num.charAt(i) - '0';
            while (!deque.isEmpty()&&k>0&&deque.peekLast()>temp){
                deque.pollLast();
                k--;
            }
            deque.add(temp);
        }
        for (int i = 0; i < k; i++) {
            deque.pollLast();
        }
        StringBuilder sb=new StringBuilder();
        while (!deque.isEmpty()){
            Integer i = deque.pollFirst();
            if(i==0&&sb.length()==0){
                continue;
            }else {
                sb.append(i);
            }
        }
        return sb.length()==0?"0":sb.toString();
    }
}
