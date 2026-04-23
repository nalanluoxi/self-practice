package likou;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：螺旋矩阵2
 * @Date：2025/3/26 20:56
 * @Filename：螺旋矩阵2
 */
public class 螺旋矩阵2 {
    public static void main(String[] args) {
        int[][] num = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        List<Integer> integers = spiralOrder(num);
        System.out.println(integers);
    }

    static List<Integer> ans;
    static int[][] nums;
    public static List<Integer> spiralOrder(int[][] num) {
        ans=new ArrayList<>();
        nums=num;
        int x = nums.length;
        int y = nums[0].length;
        int left = 0;
        int right = y - 1;
        int top = 0;
        int bottom = x - 1;
        while (true){
            leftToRight(left,right,top);
            top++;
            if (top>bottom){
                break;
            }
            topToBottom(right,top,bottom);
            right--;
            if (left>right){
                break;
            }
            rightToLeft(right,left,bottom);
            bottom--;
            if (top>bottom){
                break;
            }
            bottomToTop(left,top,bottom);
            left++;
            if (left>right){
                break;
            }
        }
        return ans;
    }

    public static void leftToRight(int l,int r,int h){
        for (int i = l; i <=r ; i++) {
            ans.add(nums[h][i]);
        }
    }

    public static void rightToLeft(int r,int l,int h){
        for (int i = r; i >=l ; i--) {
            ans.add(nums[h][i]);
        }
    }

    public static void topToBottom(int l,int t,int b){
        for (int i = t; i <=b ; i++) {
            ans.add(nums[i][l]);
        }
    }

    public static void bottomToTop(int l,int t,int b){
        for (int i = b; i >=t ; i--) {
            ans.add(nums[i][l]);
        }
    }
}
