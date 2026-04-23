package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：删去重复
 * @Date：2025/1/21 22:07
 * @Filename：删去重复
 */
public class 删去重复 {
    public static void main(String[] args) {
        int[] num = {0,0,1,1,1,2,2,3,3,4};
        int i = removeDuplicates(num);
        System.out.println(i);
        for (int i1 = 0; i1 < num.length; i1++) {
            System.out.println(num[i1]);
        }
    }

    public static int removeDuplicates(int[] nums) {
        if (nums.length==0){
            return 0;
        }
        int fast = 1;
        int slow = 1;
        while (fast < nums.length) {
            if (nums[fast]!=nums[fast-1]){
                nums[slow]=nums[fast];
                slow++;
            }
            fast++;
        }
        return slow;
    }
}
