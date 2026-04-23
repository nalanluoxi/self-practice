package example.java2;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2
 * @Project：LanQiaoBei
 * @name：求和
 * @Date：2025/6/6 19:53
 * @Filename：求和
 */
public class 求和 {

    public static void main(String[] args) {
        String string = "1*3+3";
        System.out.println(scanAndEval(string));
        System.out.println(getans(string));
    }
    public static ArrayList<Integer> scanAndEval (String expr) {
        // write code here
        ArrayList<Integer> list = new ArrayList<>();
        for(int i=0;i<expr.length();i++){
            char c=expr.charAt(i);
            if(Character.isDigit(c)){
                int a=getans(expr.substring(0,i+1));
                list.add(a);
                //System.out.print(a+" ");
            }
        }
        return list ;
    }

    public static int getans(String str){
        int ans=0;
        char type='+';
        Deque<Integer> deque=new LinkedList<>();
        //int i=0;
        for(int i =0;i<str.length();i++){
            char c=str.charAt(i);
            if(Character.isDigit(c)){
                int num=c-'0';
                //System.out.print("当前i为： "+i+" : "+num+"\n");
                while ((i+1)<str.length()){
                    char c1 = str.charAt(i + 1);
                    if (Character.isDigit(c1)){
                        int n = c1 - '0';
                        num =num*10+n;
                    }else {
                        break;
                    }
                }
               // System.out.println("nums:"+num);
                deque.offerLast(num);
                if(type=='*'){
                    int n1=deque.pollLast();
                    int n2=deque.pollLast();
                    deque.offerLast(n1*n2);
                    type='+';
                }
            }else if(c=='*'){
                type='*';
            }
        }
        while (!deque.isEmpty()){
            ans+=deque.pollLast();
        }
        return ans;
    }
}
