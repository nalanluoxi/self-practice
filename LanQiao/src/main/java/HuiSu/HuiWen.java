package HuiSu;

import java.util.*;

public class HuiWen {

    public static void main(String[] args) {
        String s="aabcb";
        List<List<String>> partition = partition(s);
        System.out.println(partition);
    }

    static List<List<String>>ans=new ArrayList<>();
    static LinkedList<String> res=new LinkedList<>();
    static public List<List<String>> partition(String s) {
        if (s.length()<=0){
            return null;
        }
        backing(s,0);
        return ans;
    }

    public static void backing(String s,int index){
        if (index>=s.length()){
            ans.add(new ArrayList<>(res));
           // res.clear();
            return;
        }

        for (int i = index; i < s.length(); i++) {
            if (isHuiWen(s,index,i)){
                String str=s.substring(index,i+1);
                res.add(str);
        //        System.out.println("添加成功 res:"+res+" /index :"+index+" /i :"+i);
            }else {
                continue;
            }
            backing(s,i+1);
        //    System.out.println("删除前 "+res+" /index :"+index+" /i :"+i);
                res.removeLast();
        //    System.out.println("删除后"+res+" /index :"+index+" /i :"+i);
        }

    }

    public static boolean isHuiWen(String s,int sta,int end){

        for (int i = sta,j=end; i < j; i++,j--) {
            if (s.charAt(i)!=s.charAt(j)){
                return false;
            }
        }
        return true;
    }

}
