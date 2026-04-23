package likou;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：分割回文串
 * @Date：2025/2/12 16:19
 * @Filename：分割回文串
 */
public class 分割回文串 {
    public static void main(String[] args) {
        //System.out.println(isHuwen("123321"));
        System.out.println(partition("a"));
    }


    static List<List<String>> ans;
    static List<String> tans;

    public static List<List<String>> partition(String s) {
        ans=new ArrayList<>();
        tans=new ArrayList<>();
        backtracking(0,s);
        return ans;
    }

    public static void backtracking(int index,String str){
        if (index==str.length()){
            ans.add(new ArrayList<>(tans));
            return;
        }
        for (int i=index;i<str.length();i++){
            String tem = str.substring(index, i + 1);
            if (isHuwen(tem)){
                tans.add(tem);
                backtracking(i+1,str);
                tans.remove(tans.size()-1);
            }
        }
    }

    public static boolean isHuwen(String str){
        if (str.length()==0||str.length()==1){
            return true;
        }
        if (str.charAt(0)==str.charAt(str.length()-1)){
            int len = str.length();
            return isHuwen(str.substring(1,len-1));
        }
        return false;
    }
}
