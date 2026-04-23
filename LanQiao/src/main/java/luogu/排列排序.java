package luogu;

import java.util.Scanner;

public class 排列排序 {
    public static void main(String[] args) {
        paixu();
    }

    public static void paixu() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            long len = scanner.nextLong();
            int[] nums = new int[(int) (len+1)];
            for (int j = 1; j < nums.length; j++) {
                nums[j] = scanner.nextInt();
            }
            help(nums);
        }
    }


    public static void help(int[] num) {
        int lIndex = 0;
        int rIndex = 0;
        int count = 0;
        while (lIndex < num.length) {
            if (num[lIndex] == lIndex) {
                lIndex++;
                continue;
            }
            rIndex = lIndex + 1;
            int maxValue = Math.max(num[lIndex], num[rIndex]);
            while (maxValue > rIndex && rIndex < num.length) {
                rIndex++;
                maxValue = Math.max(maxValue, num[rIndex]);

            }
            count += rIndex - lIndex + 1;
            lIndex = rIndex + 1;
        }
        System.out.println(count);


    }
}
