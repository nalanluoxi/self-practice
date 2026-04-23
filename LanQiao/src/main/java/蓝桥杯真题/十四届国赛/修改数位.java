package 蓝桥杯真题.十四届国赛;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.十四届国赛
 * @Project：LanQiaoBei
 * @name：修改数位
 * @Date：2025/6/14 21:38
 * @Filename：修改数位
 */
public class 修改数位 {
    public static void main(String[] args) {
        Scanner  scanner=new Scanner(System.in);
        String string = scanner.nextLine();
        int min=Integer.MAX_VALUE;
        for (int i = 0; i < string.length()-9; i++) {
            char[] charArray = string.substring(i, i + 10).toCharArray();
            int sum=0;
            Arrays.sort(charArray);
            for (int j = 0; j < charArray.length; j++) {
                int i1 = charArray[j] - '0' - j;
                sum+=Math.abs(i1);
            }
            min=Math.min(min,sum);
        }
        System.out.println(min);
    }



        static String m;
        public static void test(){
            Scanner sc=new Scanner(System.in);
            m=sc.next();
            int l=m.length();
            int min=Integer.MAX_VALUE;

            for (int i = 0; i < l-9; i++) {
                char [] arr=m.substring(i, i+10).toCharArray();

                Arrays.sort(arr);
                int sum=0;
                for (int j = 0; j < 10; j++) {
                    int a=Integer.parseInt(arr[j]+"");
                    if(a!=j) {
//                    System.out.println(arr[j]-'0'-1);
                        sum+=Math.abs(a-j);
                    }
                }
                min=Math.min(min, sum);
            }
            System.out.println(min);


        }
    }

