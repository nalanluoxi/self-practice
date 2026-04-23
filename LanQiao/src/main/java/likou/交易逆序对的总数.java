package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：交易逆序对的总数
 * @Date：2025/7/6 10:37
 * @Filename：交易逆序对的总数
 */
public class 交易逆序对的总数 {
    public static void main(String[] args) {
        int[] ints = {9, 7, 5, 4, 6};
        System.out.println(reversePairs(ints));
    }

    public static int reversePairs(int[] record) {
        if (record.length<2){
            return 0;
        }
        int[]temp=new int[record.length];
        return partition(record,0,record.length-1,temp);
    }
    public static int partition(int[]nums,int left,int right,int[]temp){
        if (left==right){
            return 0;
        }
        int mid = left + (right - left) / 2;
        int leftnum = partition(nums, left, mid, temp);
        int rightnum = partition(nums, mid + 1, right, temp);
        if (nums[mid]<=nums[mid+1]){
            return leftnum+rightnum;
        }
        int add = addTwo(nums, left, mid, right, temp);
        return add+leftnum+rightnum;
    }

    public static int addTwo(int []nums,int left,int mid,int right,int[]temp){
        for (int i=left;i<=right;i++){
            temp[i]=nums[i];
        }
        int i=left,j=mid+1;
        int count=0;
        for (int k=left;k<=right;k++){
            if (i==mid+1){
                nums[k]=temp[j++];
            } else if (j==right+1) {
                nums[k]=temp[i++];
            } else if (temp[i] <= temp[j]) {
                nums[k]=temp[i++];
            }else {
                nums[k]=temp[j++];
                int t = mid - i + 1;
                count+=t;
            }
        }

        return count;
    }
}
