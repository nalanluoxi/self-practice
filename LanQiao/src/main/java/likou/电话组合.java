package likou;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：电话组合
 * @Date：2025/1/21 18:45
 * @Filename：电话组合
 */
public class 电话组合 {
    public static void main(String[] args) {
        List<String> strings = letterCombinations("23");

    }

    static  String[] map = new String[]{
            "",     // 0
            "",     // 1
            "abc",  // 2
            "def",  // 3
            "ghi",  // 4
            "jkl",  // 5
            "mno",  // 6
            "pqrs", // 7
            "tuv",  // 8
            "wxyz"  // 9
    };
    public static List<String> letterCombinations(String digits) {
        if (digits.equals("")){
            return new ArrayList<>();
        }
        List<String> res=new ArrayList<>();
        List<String> temp=new ArrayList<>();
        for (int i = 0; i < digits.length(); i++) {
            int index= digits.charAt(i)-48;
            temp.add(map[index]);
        }
        help(temp, 0, "",res);
        return res;
    }
    public static void help(List<String> temp,int index,String ans,List<String> res){
        if (index==temp.size()){
            res.add(new String(ans));
            return;
        }
        String str = temp.get(index);
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            help(temp,index+1,ans+c,res);
        }
    }

}
