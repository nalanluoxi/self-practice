package likou.力扣test2;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：Test0926
 * @Date：2025/9/26 22:16
 * @Filename：Test0926
 */
public class Test0926 {
    public static void main(String[] args) {
        getMax(new int[]{1,2,3,4,5,6,7,8,9,10}, 1,3);
    }

    public static void getMax(int[] nums,int l,int r){
        int inl=0;
        int inr=nums.length-1;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i]==l){
                inl=i;
                break;
            }
        }
        for (int i = nums.length-1; i> 0;i --) {
            if (nums[i]==r){
                inr=i;
                break;
            }
        }
        int[] arr=new int[inr-inl+1];
        for (int i = 0; i <arr.length ; i++) {
            arr[i]=nums[i+inl];
        }
        Arrays.sort(arr);
        System.out.println(arr[0]);
    }
}
