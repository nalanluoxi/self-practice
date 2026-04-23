package likou;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：接雨水
 * @Date：2025/1/12 16:00
 * @Filename：接雨水
 */
public class 接雨水 {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        Hashtable<String ,String> stringStringHashtable = new Hashtable<>();
    }

    public int trap(int[] height) {
        int count=0;
        int left=0;
        int leftheight=0;
        int right=0;
        int rightheight=0;


        for (int i = 0; i < height.length; i++) {
            if (height[i]!=0){
                left=i;
                leftheight=height[i];
            }
        }


        return count;
    }
}
