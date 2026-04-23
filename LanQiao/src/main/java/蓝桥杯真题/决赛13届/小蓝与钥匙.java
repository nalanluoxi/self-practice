package 蓝桥杯真题.决赛13届;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.决赛13届
 * @Project：LanQiaoBei
 * @name：小蓝与钥匙
 * @Date：2025/4/8 11:31
 * @Filename：小蓝与钥匙
 */
public class 小蓝与钥匙 {

    static int[] nums;
    static int ans;
    public static void main(String[] args) {
        nums= new int[]{14,13,12,11,10,9,8,7,6,5,4,3,2,1};
        ans=0;
        backtrack(0);
        System.out.println(ans);
    }
    public static void pringtAll(int[] nums){
        System.out.print("[");
        for (int num : nums) {
            System.out.print(num+" ");
        }
        System.out.println("]");
    }
    public static void backtrack(int index){
        if (index==nums.length-1){
            if (isOk(nums)){
                ans++;
                pringtAll(nums);
            }
            return;
        }
        for (int i = index; i < nums.length; i++) {
            swap(index,i);
            backtrack(index+1);
            swap(index,i);
        }
    }
    public static void swap(int i,int j){
        int temp=nums[i];
        nums[i]=nums[j];
        nums[j]=temp;
    }

    public static boolean isOk(int[] nums){
        for (int i = 0; i < nums.length; i++) {
            if (nums[i]==i+1){
                return false;
            }
        }
        return true;
    }

    public static int jie(int i){
        if (i==1||i==0){
            return 1;
        }
        return i*jie(i-1);
    }
}
