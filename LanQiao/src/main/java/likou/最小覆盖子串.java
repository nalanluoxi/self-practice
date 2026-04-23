package likou;

import PTA.舍入;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：最小覆盖子串
 * @Date：2025/4/25 17:56
 * @Filename：最小覆盖子串
 */
public class 最小覆盖子串 {
    public static void main(String[] args) {
        System.out.println(minWindow("ADOBECODEBANC","ABC"));
    }

    static HashMap<Character,Integer> map;
    static int mapcount;

    public static void init(String t){
        map = new HashMap<>();
        for (int i = 0; i < t.length(); i++) {
            char c = t.charAt(i);
            if (map.containsKey(c)){
                map.put(c,map.get(c)+1);
            }else {
                map.put(c,1);
            }
        }
        mapcount = map.size();
    }


    public static String minWindow(String s, String t) {
        init(t);
        String ans = "";
        int anslen=Integer.MAX_VALUE;
        int left = 0;
        int right = 0;
        while (right<s.length()){
            char c = s.charAt(right);
            if (map.containsKey(c)){
                map.put(c,map.get(c)-1);
                if (map.get(c)==0){
                    mapcount--;
                }
            }
            right++;
            while (mapcount==0){
                String temp = s.substring(left, right);
                if (temp.length()<anslen){
                    anslen = temp.length();
                    ans = temp;
                }
                char d = s.charAt(left);
                if (map.containsKey(d)){
                    map.put(d,map.get(d)+1);
                    if (map.get(d)==1){
                        mapcount++;
                    }
                }
                left++;
            }
        }
        return ans;
    }


}
