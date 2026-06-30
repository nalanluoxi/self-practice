package 面试;

import java.util.*;

public class Test0624 {



    public static void main(String[] args) {
        int[][]intervals = {{1,3},{2,6},{8,10},{15,18}};
        test1(intervals);
        System.out.println("-----------------");
        int[][]nums2={{1,4},{4,5}};
        test1(nums2);
        System.out.println("-----------------");




        String str1="172.16.254.1";
        test2(str1);
        System.out.println("-----------------");
        String str2="256.256.256.256";
        test2(str2);
        System.out.println("-----------------");
        String str3="2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        test2(str3);
        System.out.println("-----------------");
        String str4="02001:0db8:85a3:0000:0000:8a2e:0370:7334";
        test2(str4);
    }

    public static void test1(int[][] nums){
        Queue<int[]> queue=new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] a1, int[] a2) {
                return a1[0]-a2[0];
            }
        });
        for (int[] num : nums) {
            queue.add(num);
        }

        List<int[]> list=new ArrayList<>();

        for (int[] num : queue) {
            if (list.size()==0){
                list.add(num);
            }else {
                int[] last = list.get(list.size() - 1);
                if (num[0]<=last[1]){
                    list.remove(list.size()-1);
                    list.add(new int[]{Math.min(num[0],last[0]),Math.max(num[1],last[1])});
                }else {
                    list.add(num);
                }
            }
        }
        for (int[] ints : list) {
            System.out.println(ints[0]+" "+ints[1]);
        }
    }

    /*
给定一个字符串，你需要验证它是否是一个合法的IP地址，并输出结果"IPv4"、"IPv6" 或者"Neither"。
IPv4 合法条件
IPv4 地址格式为 x.x.x.x，其中 x 是一个 0~255 的十进制整数，且：
恰好由 3 个 . 分隔为 4 组
每组是 0~255 的整数
不允许前导零（如 01、00 非法，0 合法）
每组不能为空，不能包含非数字字符
IPv6 合法条件
IPv6 地址格式为 x:x:x:x:x:x:x:x，其中 x 是一个 1~4 位的十六进制字符串，且：
恰好由 7 个 : 分隔为 8 组
每组是 1~4 个十六进制字符（0-9, a-f, A-F）
允许前导零（如 0001、00、0 都合法）
每组不能为空，不能超过 4



     */

    public static void test2(String str){
        if (str.contains(".")){
            String[] split = str.split("\\.");
            checkIPV4(split);
        }else if (str.contains(":")){
            String[] split = str.split(":");
            checkIpV6(split);
        }else {
            System.out.println("Neither");
        }
    }

    public static void checkIPV4(String [] strings){
        if (strings.length!=4){
           // System.out.println(strings.length );
            System.out.println("Neither");
            return;
        }
        for (String string : strings) {
            if (string.length()==1 && "0".equals(string)){
                continue;
            }else if (string.length()>1 && string.charAt(0)=='0'){
                System.out.println("Neither");
                return;
            } else if (string.length() > 1 && string.length() <= 3) {
                Integer i = Integer.valueOf(string);
                if (i>255||i<0){
                    System.out.println("Neither");
                    return;
                }
            }
        }
        System.out.println("IPv4");

    }

    public static void checkIpV6(String[] strings){
        if (strings.length!=8){
            System.out.println("Neither");
            return;
        }
        for (String string : strings) {
            if (string.length()>4){
                System.out.println("Neither");
                return;
            } else if (string.length() >= 1 && string.length() <= 4) {
                if (!check6(string)){
                    System.out.println("Neither");
                    return;
                }
            }else {
                System.out.println("Neither");
                return;
            }
        }
        System.out.println("IPv6");

    }

    public static boolean check6(String str){
        if (str.length()<=0 || str.length()>4){
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isDigit(c) || (c>='a' && c<='f') || (c>='A' && c<='F')){

            }else {
                return false;
            }
        }
        return true;
    }

}
