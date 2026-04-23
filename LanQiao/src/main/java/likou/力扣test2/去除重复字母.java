package likou.力扣test2;

import likou.接雨水;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：去除重复字母
 * @Date：2025/6/4 16:55
 * @Filename：去除重复字母
 */
public class 去除重复字母 {

    public static void main(String[] args) {
        //String s = "cbacdcbc";
        String s = "ecbacba";
        System.out.println(removeDuplicateLetters(s));
        System.out.println(removeDuplicateLetters0(s));
    }


    public static String removeDuplicateLetters0(String s) {
        Deque<Character> deque=new LinkedList<>();
        int[] count=new int[26];
        boolean [] visited=new boolean[26];
        char[] list = s.toCharArray();
        for (int i = 0; i < list.length; i++) {
            count[list[i]-'a']++;
        }
        for (int i = 0; i < list.length; i++) {
            char temp = list[i];
            if (!visited[temp-'a']){
                while (!deque.isEmpty() && deque.peekLast()> temp && count[deque.peekLast()-'a']>0){
                    Character poll = deque.pollLast();
                    visited[poll-'a']=false;
                }
                deque.add(temp);
                visited[temp-'a']=true;
            }
            count[temp-'a']--;
        }
        StringBuilder str=new StringBuilder();
        while (!deque.isEmpty()){
            str.append(deque.poll());
        }
        return str.toString();
    }
    public static String removeDuplicateLetters(String s) {
        int[] hash=new int[26];
        char[] stack=new char[26];
        boolean[] visited=new boolean[26];
        int r=0;
        char[] charArray = s.toCharArray();
        int length = charArray.length;
        for (int i = 0; i < length; i++) {
            hash[charArray[i]-'a']++;
        }
        Arrays.fill(visited,false);
        for (int i = 0; i < length; i++) {
            char c = charArray[i];
            if (!visited[c-'a']){
                while (r>0 && stack[r-1]>c && hash[stack[r-1]-'a']>0 ){
                    visited[stack[--r]-'a']=false;
                }
                stack[r++]=c;
                visited[c-'a']=true;
            }
            hash[c-'a']--;
        }
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < r; i++) {
            str.append(stack[i]);
        }
        return str.toString();
    }

  /*  public static String removeDuplicateLetters(String s) {
        Deque<Character> deque = new LinkedList<>();
        int[] hash = new int[26];
        char[] charArray = s.toCharArray();
        String ans = "";
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            while (!deque.isEmpty() && hash[c - 'a'] != 0) {
                if (deque.size() > ans.length()) {
                    ans = getString(deque);
                }
                Character c1 = deque.pollFirst();
                hash[c1 - 'a'] = 0;
            }
            deque.add(c);
            hash[c - 'a'] = 1;
        }
        while (!deque.isEmpty()) {
            if (deque.size() > ans.length()) {
                ans = getString(deque);
            }
            Character c = deque.pollFirst();
            hash[c - 'a'] = 0;
        }
        return ans;

    }

    public static String getString(Deque<Character> deque) {
        String string = "";
        for (Character character : deque) {
            string += character;
        }
        return string;
    }*/
}
