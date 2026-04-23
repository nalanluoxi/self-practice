package likou;

import javax.swing.plaf.PanelUI;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：全排列2
 * @Date：2025/2/6 15:12
 * @Filename：全排列2
 */
public class 全排列2 {


    static long[] temp = new long[20];

    public static void main(String[] args) {
        System.out.println(CAB(12,12));
        for (int i = 1; i <= 10; i++) {
            help(i);
        }
        helpBig(11);
        helpBig(12);
        helpBig(13);
        helpBig(14);

        System.out.println("32071101049");

    }

    public static long helpBig(long n){
        long all = jie(n);
        if (!jieMap.containsKey(n)){
            jieMap.put(n,all);
        }
        for (int i = 1; i < n; i++) {
            long cab = CAB(n, i);
            if (n-i>0){
                all-=cab*temp[(int)n-i];
            }
        }
        all--;
        System.out.println("n = " + n + "时 n0: " + all);
        temp[(int)n]=all;
        return all;
    }
    static HashMap<Long,Long> jieMap=new HashMap<>();
    public static long jie(long n){
        if (jieMap.containsKey(n)){
            return jieMap.get(n);
        }
        if (n==1||n==0){
            return 1;
        }
        return n*jie(n-1);
    }
    public static long CAB(long a, long b) {
        long ans = 1;
        for (int i = (int)a, j = 0; j < b; j++, i--) {
            ans *= i;
        }
        for (int i = (int)b; i >= 1; i--) {
            ans /= i;
        }
        return ans;
    }

    public static void help(int n) {
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = i + 1;
        }
        List<List<Integer>> lists = permuteUnique(nums);
        long n0 = 0;
        for (List<Integer> list : lists) {
            if (isNumOk(list, 0)) {
                n0++;
            }
        }

        System.out.println(lists.size());
        System.out.println("n = " + n + "时 n0: " + n0);
        temp[n] = n0;
    }

    public static boolean isNumOk(List<Integer> list, int nums) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == i + 1) {
                nums--;
            }
        }
        return nums == 0;
    }

    public static boolean isOk(List<Integer> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == i + 1) {
                return false;
            }
        }
        return true;
    }

    /*static List<List<Integer>> res;
    static List<Integer>tans;
   // static int N;
    public static List<List<Integer>> permuteUnique(int[] nums) {
        res=new ArrayList<>();
        tans=new ArrayList<>();
        for (int num : nums) {
            tans.add(num);
        }
    //    N=nums.length;
        dps(0);
        return res;
    }
    public static void dps(int index){
        if (index==tans.size()){
         *//*   if (res.contains(tans)){
                return;
            }*//*
            res.add(new ArrayList<>(tans));
        }
        for (int i = index; i <tans.size() ; i++) {
            swap(index,i);
            dps(index+1);
            swap(index,i);
        }
    }
    public  static void swap(int a,int b){
        Integer i = tans.get(a);
        tans.set(a,tans.get(b));
        tans.set(b,i);
    }*/


    static List<List<Integer>> ans;
    static List<Integer> tans;
    static boolean[] vis;

    public static List<List<Integer>> permuteUnique(int[] nums) {
        ans = new ArrayList<>();
        tans = new ArrayList<>();
        vis = new boolean[nums.length];
        Arrays.sort(nums);
        dfs(0, nums);
        return ans;
    }

    public static void dfs(int index, int[] nums) {
        if (index == nums.length) {
            ans.add(new ArrayList<>(tans));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (vis[i] || (i > 0 && nums[i] == nums[i - 1] && !vis[i - 1])) {
                continue;
            }
            vis[i] = true;
            tans.add(nums[i]);
            dfs(index + 1, nums);
            vis[i] = false;
            tans.remove(tans.size() - 1);
        }

    }

}
