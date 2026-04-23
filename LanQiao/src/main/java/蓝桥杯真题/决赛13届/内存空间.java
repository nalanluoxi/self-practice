package 蓝桥杯真题.决赛13届;

import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.决赛13届
 * @Project：LanQiaoBei
 * @name：内存空间
 * @Date：2025/4/8 16:50
 * @Filename：内存空间
 */
public class 内存空间 {
    public static void main(String[] args) {
       // System.out.println(getInt("int a=0, b=0;"));
       // System.out.println(getLong("long x=0,y=0;"));
        //System.out.println(getString("String String s1=”hello”,s2=”world”;"));
        System.out.println(getLongList("long[] arr1=new long[100000],arr2=new long[100000];"));
        /* Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String s = scanner.nextLine();
            System.out.println(s);
            String[] split = s.split(" ");
            for (int i1 = 0; i1 < split.length; i1++) {
                System.out.print(split[i1]+"   :   ");
            }
            System.out.println();
            System.out.println("=========");
        }*/
    }
    public static long getLongList(String s){
        String[] split =  s.split("[ ,]");
        for (int i = 0; i < split.length; i++) {
            System.out.print(split[i]+"  :  ");
        }


        if (!split[0].equals("long[]")){
            System.out.println("long[]匹配错误");
            return -1;
        }
        long allnums=0;



        return allnums;
    }

    public static long getInt(String s){
        String[] split =  s.split("[ ,]");
        if (!split[0].equals("int")){
            System.out.println("int匹配错误");
            return -1;
        }
        int num=0;
        for (int i = 0; i < split.length; i++) {
            if (split[i].matches(".*=.*")){
                num++;
            }
        }
        return num*4;
    }
    public static long getLong(String s){
        String[] split =  s.split("[ ,]");
        if (!split[0].equals("long")){
            System.out.println("long匹配错误");
            return -1;
        }
        int num=0;
        for (int i = 0; i < split.length; i++) {
            if (split[i].matches(".*=.*")){
                num++;
            }
        }
        return num*8;
    }

    public static long getString(String s){
        String[] split =  s.split("[ ,]");
        if (!split[0].equals("String")){
            System.out.println("String匹配错误");
            return -1;
        }
        long allnums=0;
        for (int i = 1; i < split.length; i++) {
            String nowString = split[i];
            if (!nowString.matches(".*=.*")){
                continue;
            }
            String[] strings = nowString.split("=");
            allnums+=strings[1].length();
            allnums-=2;
        }
        allnums--;
        return allnums;
    }


}
