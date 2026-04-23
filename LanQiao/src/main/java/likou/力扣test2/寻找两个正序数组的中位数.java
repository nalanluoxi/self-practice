package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：寻找两个正序数组的中位数
 * @Date：2025/5/28 22:33
 * @Filename：寻找两个正序数组的中位数
 */
public class 寻找两个正序数组的中位数 {
    public static void main(String[] args) {

    }

    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int len1 = nums1.length;
        int len2 = nums2.length;
        int total = len1 + len2;
        if (total%2==1){
            int midIndex=total/2;
            return help(nums1,nums2,midIndex+1);
        }else {
            int miindex=total/2;
            return (help(nums1,nums2,miindex)+help(nums1,nums2,miindex+1))/2.0;
        }
    }
    public static double help(int[] arr1,int [] arr2,int k){
        int len1=arr1.length;
        int len2=arr2.length;
        int index1=0;
        int index2=0;

        while (true){
            if (index1==len1){
                return arr2[index2+k-1];
            }
            else if (index2==len2){
                return arr1[index1+k-1];
            }
            else if (k==1){
                return Math.min(arr1[index1+k-1],arr2[index2+k-1]);
            }
            int half=k/2;
            int newIndex1=Math.min(index1+half,len1)-1;
            int newIndex2=Math.min(index2+half,len2)-1;
            if (arr1[newIndex1]<arr2[newIndex2]){
                k-=(newIndex1-index1+1);
                index1=newIndex1+1;

            }  else {
                k-=(newIndex2-index2+1);
                index2=newIndex2+1;
            }
        }
    }
}
