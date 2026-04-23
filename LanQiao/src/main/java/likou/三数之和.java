package likou;
import java.lang.reflect.Array;
import java.util.*;

public class 三数之和 {
    public static void main(String[] args) {
        int []nums={-1,0,1,2,-1,-4};
        List<List<Integer>> lists = threeSum(nums);
        for (List<Integer> list : lists) {
            System.out.print("[ ");
            for (Integer i : list) {
                System.out.print(i+" , ");
            }
            System.out.print("] ,");
        }
    }

    /*public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        //Set<List<Integer>> res=new HashSet<>();
        List<List<Integer>>res=new ArrayList<>();
        List<Integer> tem=new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            int a=nums[i];
            int b=0;
            int c=0;
            for (int j = i+1; j < nums.length; j++) {
                b=nums[j];
                c=-a-b;
                int left=j+1;
                int right=nums.length-1;
                while (left<=right){
                    int mid=(left+right)/2;
                    if (nums[mid]==c){
                        tem.add(a);
                        tem.add(b);
                        tem.add(c);
                        if (!res.contains(tem)){
                            res.add(new ArrayList<>(tem));
                        }
                        tem.clear();
                        break;
                    }else if (nums[mid]<c){
                        left=mid+1;
                    }else {
                        right=mid-1;
                    }
                }
            }
        }
        return res;
    }*/


    public static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> ans = new ArrayList();
        int len = nums.length;
        if(nums == null || len < 3) return ans;
        Arrays.sort(nums); // 排序
        for (int i = 0; i < len ; i++) {
            if(nums[i] > 0) break; // 如果当前数字大于0，则三数之和一定大于0，所以结束循环
            if(i > 0 && nums[i] == nums[i-1]) continue; // 去重
            int L = i+1;
            int R = len-1;
            while(L < R){
                int sum = nums[i] + nums[L] + nums[R];
                if(sum == 0){
                    ans.add(Arrays.asList(nums[i],nums[L],nums[R]));
                    while (L<R && nums[L] == nums[L+1]) L++; // 去重
                    while (L<R && nums[R] == nums[R-1]) R--; // 去重
                    L++;
                    R--;
                }
                else if (sum < 0) L++;
                else if (sum > 0) R--;
            }
        }
        return ans;
    }

    public static void help(int[]nums,List<Integer> tem){

    }
}
