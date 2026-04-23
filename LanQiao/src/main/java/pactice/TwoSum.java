package pactice;

import java.util.*;

public class TwoSum {
    public static void main(String[] args) {
        int[] nums={3,2,4};
        int target=6;
        twoSum(nums,target);
    }

//    public static int[] twoSum(int[] nums, int target) {
//        //Set<Integer>index=new HashSet<>();
//        int[]index=new int[2];
//        for (int i = 0; i < nums.length; i++) {
//            for (int j = i+1; j < nums.length; j++) {
//                if (nums[i]+nums[j]==target){
//                    index[0]=i;
//                    index[1]=j;
//                    return index;
//                }
//            }
//        }
//        return null;
//    }

    public static int[] twoSum(int[] nums, int target) {
        Map<Integer,Integer>map=new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i],i);
        }
        int []index=new int[2];
        for (int i = 0; i < nums.length; i++) {
            int temp=target-nums[i];
            if (map.containsKey(temp)&&map.get(temp)!=i){
                index[0]=i;
                index[1]=map.get(temp);

                System.out.println(i);
                System.out.println(map.get(temp));
                return index;
            }
        }
        return index;
    }
}
