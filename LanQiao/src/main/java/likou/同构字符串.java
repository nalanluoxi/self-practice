package likou;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：同构字符串
 * @Date：2025/1/24 17:26
 * @Filename：同构字符串
 */
public class 同构字符串 {
    public static void main(String[] args) {
        boolean isomorphic = isIsomorphic("abcdefghijklmnopqrstuvwxyzva", "abcdefghijklmnopqrstuvwxyzck");
        System.out.println(isomorphic);
    }
    public static  boolean isIsomorphic(String s, String t) {
        String s1 = getHash(s);
        String t1 = getHash(t);
        return s1.equals(t1);
    }

    public static String  getHash(String s){
        String string = "";
        Map<Character,Character> list=new LinkedHashMap<>();
        char index='a';
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (list.containsKey(c)){
                //System.out.println("当前字符"+c+"已经存在");
                Character temp = list.get(c);
                string+=temp;
            } else {
                string+=index;
                list.put(c,index++);
            }
        }
        //System.out.println(string);
        return string;
    }
}
