package likou.二分;

/**
 * @Author 纳兰洛熙
 * @Package：likou.二分
 * @Project：LanQiaoBei
 * @name：寻找两个正序数组的中位数
 * @Date：2025/6/24 19:56
 * @Filename：寻找两个正序数组的中位数
 */
public class 寻找两个正序数组的中位数 {
    public static void main(String[] args) {
        int[] nums1 = {1,2};
        int[] nums2 = {3,4};
        System.out.println(findMedianSortedArrays(nums1,nums2));
    }

    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int len1=nums1.length;
        int len2= nums2.length;
        if ((len1+len2)%2==1){
            return help(nums1,nums2,(len1+len2)/2+1);
        }else {
            return (help(nums1,nums2,(len1+len2)/2)+help(nums1,nums2,(len1+len2)/2+1))/2.0;
        }
    }

    public static double help(int []nums1,int []nums2,int index){
        int index1=0;
        int index2=0;
        while(true){
            if (index1== nums1.length){
                return nums2[index2+index-1];
            }
            if (index2== nums2.length){
                return nums1[index1+index-1];
            }
            if (index==1){
                return Math.min(nums1[index1],nums2[index2]);
            }
            int half=index/2;
            int nindex1=Math.min(nums1.length,index1+half)-1;
            int nindex2=Math.min(nums2.length,index2+half)-1;
            if (nums1[nindex1]<=nums2[nindex2]){
                index-=(nindex1-index1+1);
                index1=nindex1+1;
            }else {
                index-=(nindex2-index2+1);
                index2=nindex2+1;
            }
        }
    }
}
