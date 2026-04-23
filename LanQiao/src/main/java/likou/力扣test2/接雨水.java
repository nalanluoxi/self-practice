package likou.力扣test2;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：接雨水
 * @Date：2025/6/27 21:31
 * @Filename：接雨水
 */
public class 接雨水 {

    public static void main(String[] args) {
        int []he={0,1,0,2,1,0,1,3,2,1,2,1};
        System.out.println(trap(he));
    }



    static Deque<Integer> deque;
    public static int trap(int[] height) {
        deque=new LinkedList<>();
        Integer ans=0;
        for (int i = 0; i < height.length; i++) {
            while (!deque.isEmpty()&&height[i]>=height[deque.peekLast()]){
                Integer last = deque.pollLast();
                if (!deque.isEmpty()){
                    Integer left = deque.peekLast();
                    int w=i-left-1;
                    int h=Math.min(height[left],height[i])-height[last];
                    ans+=w*h;
                }
            }
            deque.addLast(i);
        }
        return ans;
    }

}
