package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：最大矩形
 * @Date：2025/2/25 10:25
 * @Filename：最大矩形
 */
public class 最大矩形 {
    public static void main(String[] args) {
        String[] matrix = {"10100", "10111", "11111", "10010"};
        int i = maximalRectangle(matrix);
        System.out.println(i);

       /* int max = getMax(new String[]{"3", "1", "3", "2", "2"});
        System.out.println(max);*/
    }

    public static int getMax(String[] arr) {
        int len = arr.length;
        int[] stack = new int[len];
        int r = 0;
        int max = 0;
        for (int i = 0; i < len; i++) {
            while (r > 0 && Integer.valueOf(arr[i]) <= Integer.valueOf(arr[stack[r - 1]])) {
                int cur = stack[--r];
                int left = r == 0 ? -1 : stack[r - 1];
                int temp = Integer.valueOf(arr[cur]) * (i - left - 1);
                max = Math.max(max, temp);
            }
            stack[r++] = i;
        }
        while (r > 0) {
            int cur = stack[--r];
            int left = r == 0 ? -1 : stack[r - 1];
            max = Math.max(Integer.valueOf(arr[cur]) * (len - left - 1), max);
        }
        return max;
    }

    public static int maximalRectangle(String[] matrix) {
        if (matrix.length == 0) {
            return 0;
        }
        String[] temp = matrix[0].split("");
        //print(temp);
        int max = getMax(temp);
        for (int i = 1; i < matrix.length; i++) {
            String str = matrix[i];
            String[] split = str.split("");
            for (int j = 0; j < split.length; j++) {
                if (!split[j].equals("0")) {
                    split[j] = String.valueOf(Integer.valueOf(split[j]) + Integer.valueOf(temp[j]));
                }
            }
            //print(split);
            max = Math.max(max,getMax(split));
            temp = split;
        }
        return max;
    }

    public static void print(String[] arr) {
        for (String s : arr) {
            System.out.print(s + " ");
        }
        System.out.println();
    }


}
