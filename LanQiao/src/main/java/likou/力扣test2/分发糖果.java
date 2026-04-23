package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：分发糖果
 * @Date：2025/7/9 12:44
 * @Filename：分发糖果
 */
public class 分发糖果 {
    public static void main(String[] args) {
        int[] nums = {1, 3, 2, 2, 1};
        int[] nums1 = {1, 2, 87, 87, 87, 2, 1};
        System.out.println(candy(nums));
    }

    public static int candy(int[] rat) {
        int[] list = new int[rat.length];
        list[0] = 1;
        for (int i = 1; i < rat.length; i++) {
            if (rat[i] > rat[i - 1]) {
                list[i] = list[i - 1] + 1;
            } else {
                list[i] = 1;
            }
        }
        for (int i = rat.length - 2; i >= 0; i--) {
            if (rat[i]>rat[i+1]){
                list[i]=Math.max(list[i],list[i+1]+1);
            }
        }
        int ans=0;
        for (int i : list) {
            ans+=i;
        }
        return ans;
    }
}
