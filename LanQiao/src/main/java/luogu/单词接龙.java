package luogu;

import java.util.Scanner;

public class 单词接龙 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Integer n = Integer.valueOf(scanner.nextLine());
        String[] str = new String[n];
        int[] num = new int[n];
        for (int i = 0; i < n; i++) {
            str[i] = scanner.nextLine();
            num[i] = 2;
        }
        String startStr = scanner.nextLine();
        char start = startStr.charAt(0);

        System.out.println("=====================================");
        for (int i = 0; i < n; i++) {
            System.out.println(str[i]+" num:"+num[i]);
        }
        System.out.println(start);
        System.out.println("=====================================");
    }
}
