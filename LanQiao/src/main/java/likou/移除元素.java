package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：移除元素
 * @Date：2025/1/21 20:47
 * @Filename：移除元素
 */
public class 移除元素 {
    public static void main(String[] args) {
        int[] nums = {3,2,2,3};
        int i = removeElement(nums, 3);
        System.out.println(i);
        System.out.println("---------");
        for (int num : nums) {
            System.out.println(num);
        }
    }

/*    public static int removeElement(int[] nums, int val) {
        int count=0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i]!=val){
                nums[count++]=nums[i];
            }
        }
        return count;
    }*/
public static int removeElement(int[] nums, int val) {
    int slow=0;
    for (int fast = 0; fast < nums.length; fast++) {
        if (nums[fast]!=val){
            nums[slow]=nums[fast];
            slow++;
        }
    }
    return slow;

}

}

