package HuiSu;

import java.util.ArrayList;
import java.util.List;

public class phone {

    public static void main(String[] args) {
        String a="23";
        List<String> strings = letterCombinations(a);
        System.out.println("结果是："+strings);
    }
    static String wo[]={"","","abc","def","ghi","jkl","mno","pqrs","tuv","wxyz"};

    static StringBuffer temp=new StringBuffer();
    static List<String> ans=new ArrayList<>();
    public static List<String> letterCombinations(String digits) {
        if (digits==null||digits.length()==0){
            return ans;
        }
        backing(digits,0);
        return ans;
    }


    public static void backing(String wods,int ind ){
        if (ind==wods.length()){
            ans.add(temp.toString());
            return;
        }

        String str=wo[wods.charAt(ind)-'0'];
        //System.out.println(str);
        for (int i = 0; i < str.length(); i++) {

            temp.append(str.charAt(i));
             backing(wods,ind+1);
            temp.deleteCharAt(temp.length()-1);
        }
    }


}
