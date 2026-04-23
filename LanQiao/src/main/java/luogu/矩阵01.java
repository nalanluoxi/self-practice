package luogu;

import java.util.Scanner;

public class 矩阵01 {
    public static void main(String[] args) {
        art();
    }

    public static void art() {
        Scanner scanner = new Scanner(System.in);
       /* int m = scanner.nextInt();//行
        int n = scanner.nextInt();//列
        int k = scanner.nextInt();*/
        long m = scanner.nextLong();
        long n = scanner.nextLong();
        long k = scanner.nextLong();

        int[] R = new int[(int) m + 1];//行
        int[] C = new int[(int) n + 1];//列
        scanner.nextLine();
        for (int i = 0; i < k; i++) {
            String[] s = scanner.nextLine().split(" ");
            String order = s[0];
            Integer index = Integer.valueOf(s[1]);
            if (order.equals("R")) {
                R = getOther(R, index);
            } else if (order.equals("C")) {
                C = getOther(C, index);
            }
        }

        int ans = 0;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                ans += (R[i] + C[j]) % 2;
            }
        }
        System.out.println(ans);
    }

    public static int[] getOther(int[] nums, int index) {
        if (nums[index] == 1) {
            nums[index] = 0;
        } else if (nums[index] == 0) {
            nums[index] = 1;
        }

        return nums;
    }


}
