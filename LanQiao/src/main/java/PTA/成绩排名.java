package PTA;

import java.util.Scanner;

public class 成绩排名 {
    public static void main(String[] args) {
        paiming();
    }

    public static void paiming() {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        String maxName = "";
        String maxId = "";
        int maxGrade = 0;
        String minName = "";
        String minId = "";
        int minGrade = 0;
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String[] s = scanner.nextLine().split(" ");
            if (i == 0) {
                maxGrade = Integer.valueOf(s[2]);
                minGrade = Integer.valueOf(s[2]);
            }
            if (Integer.valueOf(s[2]) >= maxGrade) {
                maxGrade = Integer.valueOf(s[2]);
                maxId = s[1];
                maxName = s[0];
            }
            if (Integer.valueOf(s[2]) <= minGrade) {
                minGrade = Integer.valueOf(s[2]);
                minId = s[1];
                minName = s[0];
            }
        }

        System.out.println(maxName + " " + maxId );
        System.out.println(minName + " " + minId );
    }
}
