package luogu;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class 产生数 {

    static ArrayList<Integer> original;
    static ArrayList<Integer> changed;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        BigInteger bigInteger = scanner.nextBigInteger();
        ArrayList<BigInteger> number = new ArrayList<>();
        while (true) {
            BigInteger temp = bigInteger.mod(BigInteger.valueOf(10));
            number.add(temp);
            BigInteger newBignum = bigInteger.divide(BigInteger.valueOf(10));
            if (newBignum.compareTo(BigInteger.valueOf(0)) == 0) {
                break;
            }
            bigInteger = newBignum;
        }

        int k = scanner.nextInt();
        scanner.nextLine();
        original = new ArrayList<>();
        changed = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            String[] s = scanner.nextLine().split(" ");
            original.add(Integer.valueOf(s[0]));
            changed.add(Integer.valueOf(s[1]));
        }








    }



}
