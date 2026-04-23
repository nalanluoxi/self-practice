package PTA;

import java.util.Scanner;

public class 子串和子列 {
    public static void main(String[] args) {
        zichuan();
    }

    public static void zichuan() {
        Scanner scanner = new Scanner(System.in);
        String string = scanner.nextLine();
        String target = scanner.nextLine();
        int l = 0, r = 0, len = 100000, n;
        for (int i = 0; i < string.length() - target.length(); i++) {
            if (string.charAt(i)==target.charAt(0)) {
                n = 1;
                for (int j = i + 1; j < string.length(); j++) {
                    if (string.charAt(j)==target.charAt(n)) {
                        n++;
                    }
                    if (n == target.length()) {
                        if (j - i < len) {
                            len = j - i;
                            l = i;
                            r = j;
                        }
                        break;
                    }
                }
            }
        }


        System.out.println(string.substring(l,r+1));

    }
}

