package example.java2.排序;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2.排序
 * @Project：LanQiaoBei
 * @name：颜色分类
 * @Date：2025/6/10 9:19
 * @Filename：颜色分类
 */
public class 颜色分类 {

    public static void main(String[] args) {
        int[]nums=new int[]{2,0,2,1,1,0};
        sortColors(nums);
        for (int i : nums) {
            System.out.println(i);
        }
    }
    public static void sortColors(int[] nums) {
        int l=0,r=nums.length-1,h=0;
        while(l<r){
            if(nums[l]==0){
                if(l==h){
                    l++;
                }else {
                swap(nums,l,h);
                h++;
                }
            }else if(nums[l]==2){
                swap(nums,l,r);
                h++;
            }else{
                l++;
            }
        }
    }

    public static void swap(int[]nums,int i,int j){
        int t=nums[i];
        nums[i]=nums[j];
        nums[j]=t;
    }
}
