package HuiSu;

import java.util.ArrayList;
import java.util.List;

public class IPadress {

    public static void main(String[] args) {
        String s="25525511135";
        List<String> strings = restoreIpAddresses(s);
        System.out.println(strings);
    }
    public static List<String> restoreIpAddresses(String s) {
        if (s.length()>12){
            return ans;
        }
        backing(s,0,0);
        return ans;
    }
    static  List<String> ans=new ArrayList<>();
    public  static void backing(String s,int index,int num){

        if (num==3){
            if (isID(s,index,s.length()-1)){
                ans.add(s);
            }
            //ans.add(s);
            return;
        }

        for (int i = index; i < s.length(); i++) {

            if (isID(s,index,i)){
                s=s.substring(0,i+1)+"."+s.substring(i+1);
                num++;
                backing(s,i+2,num);
                num--;
                s=s.substring(0,i+1)+s.substring(i+2);
            }else {
                break;
            }


        }
    }

    public static boolean isID(String s,int index,int end){

        if (index>end){
            return false;
        }
        if (s.charAt(index)=='0'&&index!=end){
            return false;
        }

        String str=s.substring(index,end+1);
        int num= 0;

        try {
            num= Integer.parseInt(str);
        }catch (NumberFormatException e) {

            num=0;
        }

        System.out.println(num);
        if (num>225||num<0){
            return false;
        }

        /*int num = 0;
        for (int i = index; i <= end; i++) {
            if (s.charAt(i) > '9' || s.charAt(i) < '0') { // 遇到⾮数字字符不合法
                return false;
            }
            num = num * 10 + (s.charAt(i) - '0');
            if (num > 255) { // 如果⼤于255了不合法
                return false;
            }
        }*/
        return true;

    }

}
