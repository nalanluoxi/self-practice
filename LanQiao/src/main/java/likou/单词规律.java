package likou;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：单词规律
 * @Date：2025/2/2 16:07
 * @Filename：单词规律
 */
public class 单词规律 {
    public static void main(String[] args) {
        boolean b = wordPattern("abba", "dog cat cat dog");
        System.out.println(b);
    }

    public static boolean wordPattern(String pattern, String s) {
        String[] str1 = s.split(" ");
        String[] str2 = pattern.split("");
        String s1 = getHashString(str1);
        String s2 = getHashString(str2);
        if (s1.equals(s2)){
            return true;
        }
        return false;
    }

    public static String getHashString(String[] s){
        String res="";
        char tempchar = 'a'-1;
        Map<String,Character> map = new HashMap<>();
        for (int i = 0; i < s.length; i++) {
            if (map.containsKey(s[i])){
                res+=map.get(s[i]);
            }else {
                tempchar++;
                map.put(s[i],tempchar);
                res+=tempchar;
            }
        }
       // System.out.println(res);
        return res;
    }
}
