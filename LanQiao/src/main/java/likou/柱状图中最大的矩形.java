package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：柱状图中最大的矩形
 * @Date：2025/2/24 21:09
 * @Filename：柱状图中最大的矩形
 */
public class 柱状图中最大的矩形 {

    public static void main(String[] args) {
        int i = largestRectangleArea(new int[]{2, 4});
        System.out.println(i);
    }

    static int[] stack;
    static int r;

    public static int largestRectangleArea(int[] heights) {
        int len = heights.length;
        stack = new int[len];
        r = 0;
        int max = -1;
        for (int i = 0; i < len; i++) {
            while (r > 0 && heights[i] <= heights[stack[r - 1]]) {
                int cur = stack[--r];
                int left = r == 0 ? -1 : stack[r - 1];
                int temp = heights[cur] * (i - left - 1);
                max = Math.max(max, temp);
            }
            stack[r++] = i;
        }
        while (r > 0) {
            int cur = stack[--r];
            int left = r == 0 ? -1 : stack[r - 1];
            max = Math.max(heights[cur] * (len - left - 1), max);
        }
        return max;
    }
}
