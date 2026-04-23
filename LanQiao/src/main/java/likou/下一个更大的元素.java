package likou;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：下一个更大的元素
 * @Date：2025/3/10 17:44
 * @Filename：下一个更大的元素
 */
public class 下一个更大的元素 {

    public static void main(String[] args) {
        int[] ints = nextGreaterElement(new int[]{4, 1, 2}, new int[]{1, 3, 4, 2});
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }

    static Map<Integer, Integer> map;
    static Deque<Integer> deque;
    static int[] ans;

    public static int[] nextGreaterElement(int[] nums1, int[] nums2) {
        int len1 = nums1.length;
        int len2 = nums2.length;
        map = new HashMap<>();
        deque = new LinkedList<>();
        ans = new int[Math.min(len1, len2)];
        Arrays.fill(ans, -1);
        for (int i = 0; i < len1; i++) {
            map.put(nums1[i],i);
        }
        deque.offerLast(0);
        for (int i = 1; i < len2; i++) {
            if (!deque.isEmpty()&&nums2[i]<nums2[deque.peekLast()]){
                deque.offerLast(i);
            }else {
                while (!deque.isEmpty() && nums2[i]>nums2[deque.peekLast()]){
                    Integer last = deque.pollLast();
                    if (map.containsKey(nums2[last])){
                        ans[map.get(nums2[last])]=nums2[i];
                    }
                }
                deque.offerLast(i);
            }
        }
        return ans;
    }


    /*static Deque<Integer> de1;
    static Map<Integer,Integer> map;
    public static int[] nextGreaterElement(int[] nums1, int[] nums2) {
        de1=new LinkedList<>();
        map=new HashMap<>();
        for (int i = 0; i < nums2.length; i++) {
            map.put(nums2[i],i);
        }
        int len1 = nums1.length;
        int len2 = nums2.length;
        int[] ans = new int[Math.min(len1,len2)];
        Arrays.fill(ans,-1);
        for (int i = 0 ; i < len1; i++) {
            while (!de1.isEmpty() && nums1[i]>nums1[de1.peekLast()]){
                Integer last = de1.pollLast();
                Integer index = map.get(nums1[last]);
                for (int j = index+1; j < len2; j++) {
                    if (nums2[j]>nums1[last]){
                        ans[last]=nums2[j];
                        break;
                    }
                }
            }
            de1.offerLast(i);
        }
        while (!de1.isEmpty()){
            Integer last = de1.pollLast();
            Integer index = map.get(nums1[last]);
            for (int j = index+1; j < len2; j++) {
                if (nums2[j]>nums1[last]){
                    ans[last]=nums2[j];
                    break;
                }
            }
        }
        return ans;
    }*/
}
