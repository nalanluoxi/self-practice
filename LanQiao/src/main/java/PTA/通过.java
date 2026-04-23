package PTA;


import javax.print.DocFlavor;
import java.util.Scanner;

public class 通过 {

    public static void main(String args[]) {
        guo();
    }

    static Scanner scanner = new Scanner(System.in);

    public static void guo() {
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String str = scanner.nextLine();
            pandian(str);
        }
    }

    private static void pandian(String str) {
        String[] s = str.split("");
        int a1 = 0, a2 = 0, a3 = 0, p = 0, t = 0;
        for (String string : s) {
            if (string.equals("A")) {
                if (p == 0) {
                    a1++;
                } else if (p != 0 && t == 0) {
                    a2++;
                } else if (p != 0 && t != 0) {
                    a3++;
                }
            } else if (string.equals("P")) {
                p++;
            } else if (string.equals("T")) {
                t++;
            } else {
                System.out.println("NO");
                return;
            }
        }
        if (p != 1 || t != 1) {
            System.out.println("NO");
            return;
        }
        if (a2==0){
            System.out.println("NO");
            return;
        }
        if (a1 == 0 && a3 == 0 && a2 != 0) {
            System.out.println("YES");
            return;
        }
        if (a1 * a2 == a3) {
            System.out.println("YES");
            return;
        }
        System.out.println("NO");
    }
}
