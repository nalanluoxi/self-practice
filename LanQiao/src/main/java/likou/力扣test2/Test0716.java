package likou.力扣test2;


import java.util.PriorityQueue;
import java.util.Queue;



/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：Test0716
 * @Date：2025/7/16 9:53
 * @Filename：Test0716
 */
public class Test0716 {
    public static void main(String[] args) {
/*        int [] nums1={1,0,2};
        int [] nums={1,2,2};
        int [] nums3={1,3,2,2,1};
        System.out.println(candy(nums));*/

/*        MedianFinder m=new MedianFinder();
        m.addNum(2);
        m.addNum(3);
       // m.addNum(4);
        System.out.println(m.findMedian());*/
        System.out.println(reversePairs(new int[]{9,7,5,4,6}));

    }


    public static int reversePairs(int[] record) {
        if (record.length<2){
            return 0;
        }
        int[] temp=new int[record.length];
        return partition(record,0,record.length-1,temp);
    }

    private static int partition(int[] record, int l, int r, int[] temp) {
        if (l==r){
            return 0;
        }
        int mid = l + (r - l) / 2;
        int left = partition(record, l, mid, temp);
        int right   = partition(record, mid+1, r, temp);
        return andTwo(record,l,mid,r,temp)+left+right;
    }

    private static int andTwo(int[] nums, int l, int mid, int r, int[] temp) {
        int count=0;
        for (int i=l;i<=r;i++){
            temp[i]=nums[i];
        }
        int i=l,j=mid+1;
        for (int k=l;k<=r;k++){
            if (i > mid){
                nums[k]=temp[j++];
            } else if (j > r){
                nums[k]=temp[i++];
            } else if (temp[i] <= temp[j]) {
                nums[k]=temp[i++];
            }else {
                nums[k]=temp[j++];
                int t=mid-i+1;
                count+=t;
            }
        }
        return count;
    }

    static class MedianFinder {

        private Queue<Integer> min;
        private Queue<Integer> max;

        public MedianFinder() {
            min=new PriorityQueue<>();
            max=new PriorityQueue<>((a,b)-> b-a);
        }

        public void addNum(int num) {
            if (min.size()==max.size()){
                min.add(num);
                max.add(min.poll());
            }else {
                max.add(num);
                min.add(max.poll());
            }
        }

        public double findMedian() {
            if (min.size()==max.size()){
                return (min.peek()+max.peek())/2.0;
            }
            return max.peek()*1.0;
        }
    }

    public static int candy(int[] nums) {
        int []dp=new int[nums.length] ;
        for (int i = 0; i < nums.length; i++) {
            dp[i]=1;
            if (i-1>=0&&nums[i]>nums[i-1]){
                dp[i]=Math.max(dp[i],dp[i-1]+1);
            }
        }
        for (int i = nums.length-2; i >=0; i--) {
            if (nums[i]>nums[i+1]){
                dp[i]=Math.max(dp[i],dp[i+1]+1);
            }
        }
        int ans=0;
        for (int i : dp) {
            ans+=i;
        }

        return ans;
    }
}
