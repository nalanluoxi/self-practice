package pactice;

import java.util.HashMap;
import java.util.Map;

public class FourSumCount {

    public static void main(String[] args) {
        int []nums1={1,2};
        int []nums2={-2,-1};
        int []nums3={-1,2};
        int []nums4={0,2};
        int i = fourSumCount(nums1, nums2, nums3, nums4);
        System.out.println(i);
    }

    public static int fourSumCount(int[] nums1, int[] nums2, int[] nums3, int[] nums4) {
        int count=0;
        Map<Integer,Integer>map1=new HashMap<>();
        for (int i:nums1) {
            for (int j:nums2) {
                int sum=i+j;
                map1.put(sum,map1.getOrDefault(sum,0)+1);
            }
        }

        for (int i:nums3) {
            for (int j:nums4) {
                int sum2=i+j;
                count+=map1.getOrDefault(0-sum2,0);
            }
        }
        return count;
    }
}
