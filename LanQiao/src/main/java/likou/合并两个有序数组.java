package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：合并两个有序数组
 * @Date：2025/3/24 16:19
 * @Filename：合并两个有序数组
 */
public class 合并两个有序数组 {
    public static void main(String[] args) {
        //   int[] nums1={1,2,3,0,0,0};
        int[] nums1 = {4, 5, 6, 0, 0, 0};
        int[] nums2 = {1, 2, 3};
        merge(nums1, 3, nums2, 3);
    }

    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        int l = m-1;
        int r = n-1;
        int len=nums1.length-1;
        while (l>=0 && r>=0){
            if (nums1[l]>=nums2[r]){
                nums1[len--]=nums1[l--];
            }else {
                nums1[len--]=nums2[r--];
            }
        }
        while (l>=0){
            nums1[len--]=nums1[l--];
        }
        while (r>=0){
            nums1[len--]=nums2[r--];
        }
        for (int i = 0; i < nums1.length; i++) {
            System.out.print(nums1[i] + " ");
        }
    }

    public static void swap(int[] num1, int[] num2, int i, int j) {
        int temp = num1[i];
        num1[i] = num2[j];
        num2[j] = temp;
    }
}
