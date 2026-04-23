package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：最大宽度坡
 * @Date：2025/2/25 11:34
 * @Filename：最大宽度坡
 */
public class 最大宽度坡 {
    static int[] stack;
    static int r;

    public static void main(String[] args) {
      //  System.out.println(maxWidthRamp(new int[]{6, 0, 8, 2, 1, 5}));
        System.out.println(maxWidthRamp(new int[]{9,8,1,0,1,9,4,0,4,1}));
    }

    public static int maxWidthRamp(int[] nums) {
        int len = nums.length;
        stack = new int[len];
        r = 1;
        int max = 0;
        stack[0]=0;
        for (int i = 1; i < len; i++) {
            if (nums[i] < nums[stack[r - 1]]) {
                stack[r++] = i;
            }
        }
        for (int i = len - 1; i >= 0; i--) {
            while (r > 0 && nums[i] > nums[stack[r - 1]]) {
                max = Math.max(max, (i - stack[--r]));
            }
        }
        return max;
    }

}
