package likou.贪心;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou.贪心
 * @Project：LanQiaoBei
 * @name：分发糖果
 * @Date：2025/3/13 21:15
 * @Filename：分发糖果
 */
public class 分发糖果 {
    public static void main(String[] args) {
        System.out.println(candy(new int[]{1,2,87,87,87,2,1}));
      //  System.out.println(candy(new int[]{1, 0, 2}));

    }

    public static int candy(int[] ratings) {
        int [] cans=new int[ratings.length];
        Arrays.fill(cans,1);
        for (int i = 1; i < ratings.length-1; i++) {
            if (ratings[i]>ratings[i-1]){
                cans[i]=cans[i-1]+1;
            }
        }
        for (int i = ratings.length-2;i>=0; i--) {
            if (ratings[i]>ratings[i+1]){
                cans[i]=cans[i+1]+1;
            }
        }
        int ans=0;
        for (int i = 0; i < cans.length; i++) {
            ans+=cans[i];
        }
        return ans;
    }
}
