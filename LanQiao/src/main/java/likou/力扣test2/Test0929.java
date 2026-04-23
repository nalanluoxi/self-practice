package likou.力扣test2;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：Test0929
 * @Date：2025/9/29 22:16
 * @Filename：Test0929
 */
public class Test0929 {
    public static void main(String[] args) {
        String string = "3[a]2[bc]";
        System.out.println(decodeString(string));
    }

    public static String decodeString(String s) {
        LinkedList<String> stack = new LinkedList<>();
        int len = s.length();
        int i=0;
        while (i<len){
            char c = s.charAt(i);
            if (Character.isDigit(c)){
                String num="";
                while (Character.isDigit(s.charAt(i))){
                    num+=s.charAt(i);
                    i++;
                }
                stack.addLast(num);
            } else if (Character.isLetter(c) || c == '[') {
                stack.addLast(String.valueOf(c));
                i++;
            }else {
                i++;
                LinkedList<String> sub = new LinkedList<>();
                while (!stack.peekLast().equals("[")){
                    String string = stack.pollLast();
                    sub.addLast(string);
                }
                stack.pollLast();
                Collections.reverse(sub);
                String string = getStirng(sub);
                Integer num = Integer.valueOf(stack.isEmpty() ?"1" : stack.pollLast());
                StringBuilder stringBuilder = new StringBuilder();
                while (num!=0){
                    stringBuilder.append(string);
                    num--;
                }
                stack.addLast(stringBuilder.toString());
            }
        }
        return getStirng(stack);
    }
    public static String getStirng(LinkedList<String>sub){
        StringBuilder sb=new StringBuilder();
        for (String s : sub) {
            sb.append(s);
        }
        return sb.toString();
    }
    public static int[] dailyTemperatures(int[] nums) {
        int[]ans=new int[nums.length];
        Deque<Integer> deque=new LinkedList<>();
        for (int i = 0; i < nums.length; i++) {
            while (!deque.isEmpty() && nums[i] >nums[deque.peekLast()]){
                Integer last = deque.pollLast();
                ans[last]=i-last;
            }
            deque.offerLast(i);
        }
        while (!deque.isEmpty()){
            ans[deque.pollLast()]=0;

        }
        return ans;
    }
}
