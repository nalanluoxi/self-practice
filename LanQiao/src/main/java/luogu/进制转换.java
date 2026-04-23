package luogu;

import java.util.ArrayList;
import java.util.Scanner;

public class 进制转换 {
    public static void main(String[] args) {
        zhuanhuan();
    }


    public static void zhuanhuan() {
        Scanner scanner = new Scanner(System.in);
        Long number = scanner.nextLong();
        Long base = scanner.nextLong();
        System.out.print(number+"=");

        ArrayList<Character> res = new ArrayList<>();
        char[] list = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        while (number != 0) {
            long temp = number % base;
            number = number / base;
            if (temp < 0) {
                /*temp =temp-base;
                number++;*/
                temp=-temp;
            }
            res.add(list[(int) temp]);
        }


        for (int i = res.size() - 1; i >= 0; i--) {
            System.out.print(res.get(i));
        }
        System.out.println("(base"+base+")");

    }
}
