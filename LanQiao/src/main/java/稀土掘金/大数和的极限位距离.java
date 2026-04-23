package 稀土掘金;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：大数和的极限位距离
 * @Date：2025/1/20 9:26
 * @Filename：大数和的极限位距离
 */
public class 大数和的极限位距离 {


    public static void main(String[] args) {
        List<Integer> arr=new ArrayList<>();
        //  You can add more test cases here
         //System.out.println(solution("111", "222") == 0);
        //System.out.println(solution("111", "34") == 1);
        System.out.println(solution("2222222222222222222222", "1") == 0);
        /*int num = getNum("123456");
        System.out.println(num);*/
        //System.out.println(solution("5976762424003073", "6301027308640389") == 6);

    }

    public static int solution(String string1, String string2) {
        // Please write your code here
        String sum = addString(string1, string2);
        

        return 0;
    }

    public static String addString(String string1,String string2){
        int len= Math.max(string1.length(),string2.length());
        String res="";
        int other=0;
        for (int i = 0; i <len; i++) {
            int num1=0;
            int num2=0;
            if (i<string1.length()){
                num1= string1.charAt(string1.length()-i-1)-48;
            }

            if (i<string2.length()) {
                num2 = string2.charAt(string2.length()-i-1)-48;
            }
            int temp = num1 + num2;
            if (other!=0){
                temp+=other;
                other=0;
            }

            if (temp<10){
                res=temp+res;
            }else {
                other=temp/10;
                res=temp%10+res;
            }


        }
        return res;
    }
}
