package pactice;

import java.util.ArrayList;
import java.util.Arrays;

public class IsAnagram {
    public static void main(String[] args) {

        String s="anagram";
        String t="nagaram";
        isAnagram(s,t);
    }

    public static boolean isAnagram(String s, String t) {
        int[] record=new int[26];
        System.out.println(s.length());
        for (int i = 0; i < s.length(); i++) {
            record[s.charAt(i)-'a']++;
        }

        for (int i = 0; i < t.length(); i++) {
            record[t.charAt(i)-'a']--;
        }

        for (int count:record
             ) {
            if (count!=0){
                System.out.println("错误");
                return false;
            }
        }
        System.out.println("运行正确");
        return true;
    }
}
