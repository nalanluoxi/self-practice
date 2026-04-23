package pactice;

import java.util.*;

public class Intersection {
    public static void main(String[] args) {

        int[]num1={1,2,2,1};
        int[]num2={2,2};
        intersection(num1,num2);

    }
  /*  public  static int[] intersection(int[] nums1, int[] nums2) {
        ArrayList<Integer>all=new ArrayList<>();
        for (int i = 0; i < nums1.length; i++) {
            for (int j = 0; j < nums2.length; j++) {
                if (nums1[i]==nums2[j]){
                    if (pactice.order(all,nums1[i])==1)
                        all.add(nums1[i]);

                }
            }
        }
        int len=all.size();
        int[]nums=new int[len];
        for (int i = 0; i < nums.length; i++) {
            nums[i]=all.get(i);
            System.out.println(nums[i]);
        }



        return nums;


    }

    public static int pactice.order(ArrayList<Integer>nums ,int one){
        for (int n:nums) {
            if (n==one){
                return -1;
            }
        }
        return 1;
    }*/

    public  static int[] intersection(int[] nums1, int[] nums2) {
        Set<Integer> newnum1=new HashSet<>();
        Set<Integer> result=new HashSet<>();

        for (Integer nu:nums1) {
            newnum1.add(nu);
        }

        for (Integer i:nums2) {
            if (newnum1.contains(i)){
                result.add(i);
            }
        }

        return result.stream().mapToInt(x->x).toArray();


    }



}
