package example.java2.排序;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2.排序
 * @Project：LanQiaoBei
 * @name：最大数
 * @Date：2025/6/9 14:44
 * @Filename：最大数
 */
public class 最大数 {
    public static void main(String[] args) {
        //int[] nums = new int[]{3, 30, 34, 5, 9};
        int[] nums = new int[]{111311,1113};
        String s = largestNumber(nums);
        String s2 = largestNumber1(nums);
        System.out.println(s);
        System.out.println(s2);
    }

    public static String largestNumber(int[] nums) {
        String[] num=new String[nums.length];
        for (int i = 0; i < nums.length; i++) {
            num[i]=String.valueOf(nums[i]);
        }
       Arrays.sort(num,(o1,o2)->(o1+o2).compareTo(o2+o1));
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < num.length; i++) {
            sb.append(num[i]);
        }
        return sb.toString();
    }

    public static String largestNumber1(int[] nums) {
        String[] str = new String[nums.length];
        for (int i = 0; i < nums.length; i++) {
            str[i]=String.valueOf(nums[i]);
        }
        Arrays.sort(str,(o1,o2)->(o2+o1).compareTo(o1+o2));
        StringBuilder ans = new StringBuilder();
        for (String string : str) {
            ans.append(string);
        }
        if (ans.toString().charAt(0)=='0'){
            return "0";
        }
        return ans.toString();
    }
}
