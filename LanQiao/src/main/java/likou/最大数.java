package likou;

import luogu.三连击;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Thread.sleep;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：最大数
 * @Date：2025/2/14 17:23
 * @Filename：最大数
 */
public class 最大数 {
    public static void main(String[] args) throws InterruptedException {
        //String string = largestNumber(new int[]{3, 30, 34, 5, 9});
        //String string = BaolilargestNumber(new int[]{3, 30, 34, 5, 9});
       // text();
        //System.out.println(string);
        System.out.println(largestNumber(new int[]{0,0}));
        System.out.println(largestNumber(new int[]{700000000,500000000}));
    }

    public static void text() throws InterruptedException {
        int time=100;
        System.out.println("==========================================");
        System.out.println("测试开始");
        int pass=0;
        int fail=0;
        for (int i = 0; i < time; i++) {
            int[] list = getList(8, 4);
            String s1 = largestNumber(list);
            String s2 = BaolilargestNumber(list);
            if (!s1.equals(s2)){
                System.out.println("出错了");
                System.out.println(Arrays.toString(list));
                sleep(1000*5);
                fail++;
            } else {
                System.out.println("正确");
                pass++;
            }
        }
        System.out.println("测试结束");
        System.out.println("==========================================");
        System.out.println("测试结果：");
        System.out.println("测试次数："+time);
        System.out.println("测试通过："+pass);
        System.out.println("测试失败："+fail);
    }

    public static int[] getList(int maxlen,int maxvalue){
        int[] arr = new int[(int)(Math.random()*maxlen)+1];
        for (int i = 0; i < arr.length; i++) {
            arr[i]=(int)(Math.random()*maxvalue)+1;
        }
        return arr;
    }

    public static String largestNumber(int[] nums) {
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


    public static String BaolilargestNumber(int[] nums) {
        String[] str = new String[nums.length];
        for (int i = 0; i < nums.length; i++) {
            str[i]=String.valueOf(nums[i]);
        }
        ArrayList<String> list = new ArrayList<>();
        backtracking(str,list,0);
        list.sort((o1,o2)->o2.compareTo(o1));
        return list.get(0);
    }

    public static void backtracking(String[]str,ArrayList<String> ans,int index){
        if (index==str.length){
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : str) {
                stringBuilder.append(s);
            }
            ans.add(new String(stringBuilder.toString()));
            return;
        }
        for (int i = index; i < str.length; i++) {
            swap(str,index,i);
            backtracking(str,ans,index+1);
            swap(str,index,i);
        }
    }

    public static void swap(String[] str,int i,int j){
        String temp = str[i];
        str[i]=str[j];
        str[j]=temp;
    }
}
