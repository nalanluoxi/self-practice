package likou.动态规划;

import likou.贪心.分发糖果;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：环绕字符串中唯一的子字符串
 * @Date：2025/4/9 14:13
 * @Filename：环绕字符串中唯一的子字符串
 */
public class 环绕字符串中唯一的子字符串 {
    public static void main(String[] args) {
        //System.out.println(findSubstringInWraproundString("zab"));
        System.out.println(findSubstringInWraproundString("cac"));
       // System.out.println(findSubstringInWraproundString("zaba"));
    }

    static int[] dp;
    public static int findSubstringInWraproundString(String s) {
        int ans=0;
        dp=new int[26];
        dp[s.charAt(0)-'a']=1;
        int len=1;
        /*char[] charArray = s.toCharArray();*/
        int[] nums=new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            nums[i]=s.charAt(i)-'a';
        }
        for (int i = 1; i < nums.length; i++) {
            if ((nums[i-1]+1)%26==nums[i]){
                len++;
            }else {
                len=1;
            }
            int now = nums[i];
            dp[now]=Math.max(dp[now],len);
        }
        for (int i : dp) {
            ans+=i;
        }
        return ans;
    }


   /* static int[] dp;
    //static Map<Character,Integer> map;
    public static int findSubstringInWraproundString(String s) {
        int ans=0;
        dp=new int[26];
        dp[s.charAt(0)-'a']=1;
        int len=1;
        //map=new HashMap<>();
        //map.put(s.charAt(0),1);
        char[] charArray = s.toCharArray();
        for (int i = 1; i < charArray.length; i++) {
            if (charArray[i]-charArray[i-1]==1 || (charArray[i]=='a' && charArray[i-1]=='z') ){
                len++;
                //dp[i]=dp[i-1]+1;
            }else {
                len=1;
                //dp[i]=1;
            }
            char c = charArray[i];
            dp[c-'a']=Math.max(dp[c-'a'],len);
            *//*if (map.containsKey(c)){
                map.put(c,Math.max(map.get(c),*//**//*dp[i]*//**//*len));
            }else {
                map.put(c,len*//**//*dp[i]*//**//*);
            }*//*
        }
        for (int i : dp) {
            ans+=i;
        }
        *//*for (Character k : map.keySet()) {
            Integer i = map.get(k);
            ans+=i;
        }*//*
        return ans;
    }*/
  /*  static Deque<Character> deque;
    static Set<String> visted;
    public static int findSubstringInWraproundString(String s) {
        int ans=0;
        deque=new LinkedList<>();
        visted=new HashSet<>();
        char[] charArray = s.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char temp = charArray[i];
            while (!deque.isEmpty() && temp-deque.peekLast()!=1 ){
                if (deque.peekLast()=='z' && temp=='a'){
                    break;
                }
                String string = deque.toString();
                //System.out.println(string);
                if (string.length()!=2 && visted.add(string.substring(1,string.length()-1))){
                    ans++;
                }
                deque.pollLast();
            }
            deque.offerLast(temp);
            if (visted.add(String.valueOf(temp))){
                ans++;
            }
        }
        while (!deque.isEmpty()){
            String string = deque.toString();
            if (string.length()!=2 && visted.add(string.substring(1,string.length()-1))){
                ans++;
            }
            deque.pollLast();
        }


        return ans;
    }*/
}
