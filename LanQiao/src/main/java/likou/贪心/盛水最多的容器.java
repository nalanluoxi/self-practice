package likou.贪心;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou.贪心
 * @Project：LanQiaoBei
 * @name：盛水最多的容器
 * @Date：2025/6/11 9:20
 * @Filename：盛水最多的容器
 */
public class 盛水最多的容器 {
    public static void main(String[] args) {
        int[] nums={1,2,1};
        System.out.println(maxArea(nums));
    }

    public static int maxArea(int[] height) {
        int ans=Integer.MIN_VALUE;
        int l=0,r=height.length-1;
        while (l<r){
            int m=Math.min(height[l],height[r])*(r-l);
            ans=Math.max(ans,m);
            if (height[l]<height[r]){
                l++;
            }else {
                r--;
            }
        }
        return ans;
    }

   /* public static int maxArea(int[] height) {
        Deque<Integer> deque =new LinkedList<>();
        int ans=Integer.MIN_VALUE;
        for(int i=0;i<height.length;i++){
            while(!deque.isEmpty()&&height[deque.peekLast()]<height[i]){
                int right=deque.pollLast();
                int left=!deque.isEmpty()?deque.peekFirst():right;
                int m=(right-left)*height[right];
                ans=Math.max(ans,m);
            }
            deque.add(i);
        }
        while(!deque.isEmpty()){
            int right=deque.pollLast();
            int left=!deque.isEmpty()?deque.peekFirst():right;
            int m=(right-left)*height[right];
            ans=Math.max(ans,m);
        }
        return ans;
    }*/
}
