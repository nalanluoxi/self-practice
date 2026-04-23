package 稀土掘金;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：不同整数
 * @Date：2025/1/12 15:24
 * @Filename：不同整数
 */
public class 不同整数 {


    public static void main(String[] args) {
        System.out.println(solution("a123bc34d8ef34") == 3);
        System.out.println(solution("t1234c23456") == 2);
        System.out.println(solution("a1b01c001d4") == 2);
    }


    public static int solution(String word) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
        String temp="";
        Set<Integer> visited=new HashSet<>();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (c>='0'&&c<='9'){
                temp+=c;
            }else {
                if (temp==""){
                    continue;
                }
                Integer tempnu = Integer.valueOf(temp);
                visited.add(tempnu);
                temp="";
            }
        }
        if (temp!=""){
            Integer tempnu = Integer.valueOf(temp);
            visited.add(tempnu);
        }
        return visited.size();
    }


}
