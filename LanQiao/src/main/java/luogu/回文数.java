package luogu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class 回文数 {
    public static void main(String[] args) {
        huiwen();
        /*Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();
        //Long n = scanner.nextLong();
        scanner.nextLine();
        String n= scanner.nextLine();
        System.out.println(n);*/
    }

    public static void huiwen() {
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();
        //Long n = scanner.nextLong();

        scanner.nextLine();
        String n= scanner.nextLine();

        List<Long> nList = new ArrayList<>();
        char[] num={'A','B','C','D','E','F'};
        long[] fnum={10,11,12,13,14,15,16};
        int order=0;
        for (int i = 0; i < n.length(); i++) {
            char temp = n.charAt(i);
            for (int j = 0; j < num.length; j++) {
                if (temp==num[j]){
                    nList.add(fnum[j]);
                    order=-1;
                    break;
                }
            }
            if (order==-1){
                order=1;
                continue;
            }
           // Long l = Long.valueOf(String.valueOf(temp));
           // System.out.println(l);
            nList.add( Long.valueOf(String.valueOf(temp)));
        }
        /*long n2 = n;
        long l = n2 % 10;
        nList.add(l);
        n2 = n2 / 10;
        while (true) {
            l = n2 % 10;
            n2 = n2 / 10;
            nList.add(l);
            if (n2 == 0) {
                break;
            }
        }*/
       // printAll(nList);

        for (int i = 0; i <= 30; i++) {
            if (isHui(nList) == 1) {
                System.out.println("STEP=" + i);
                return;
            }
            List<Long> oList = getOther(nList);
           // printAll(oList);
            nList = and(nList, oList, m);
           // System.out.print("new nList: ");
           // printAll(nList);
        }
        System.out.println("Impossible！");
    }

    public static List<Long> and(List<Long> num1, List<Long> num2, int m) {
        int Out = 0;
        List<Long> res = new ArrayList<>();
        for (int i = 0; i < num1.size(); i++) {
            long temp = num1.get(i) + num2.get(i);
            if (Out != 0) {
                temp += Out;
                Out = 0;
            }
            if (temp >= m) {
                temp -= m;
                Out++;
            }
            res.add(temp);
        }
        if (Out != 0) {
            res.add(Long.valueOf(Out));
        }
        return res;
    }

    public static int isHui(List<Long> num) {
        int last = num.size() - 1;
        int first = 0;
        while (last != first && first < last) {
            if (num.get(first) != num.get(last)) {
                return -1;
            }
            first++;
            last--;
        }
        return 1;
    }

    public static List<Long> getOther(List<Long> num) {
        int size = num.size() - 1;
        List<Long> res = new ArrayList<>();
        for (int i = size; i >= 0; i--) {
            res.add(num.get(i));
        }
        return res;
    }

    public static void printAll(List<Long> num) {
        System.out.print("[");
        int index = num.size() - 1;
        for (int i = index; i >= 0; i--) {
            System.out.print(num.get(i));
        }
        System.out.println("]");
    }

}


