package PTA;

import java.util.Scanner;

public class 舍入 {
    static Scanner scanner = new Scanner(System.in);
    static Integer digit;

    public static void main(String[] args) {
        she();
//        digit=3;
//        order2("3.1415926");


    }

    public static void she() {
        String[] s = scanner.nextLine().split(" ");
        Integer num = Integer.valueOf(s[0]);
        digit = Integer.valueOf(s[0]);
        for (Integer i = 0; i < num; i++) {
            String[] str = scanner.nextLine().split(" ");
            Integer order = Integer.valueOf(str[0]);
            if (order==1){
                order1(str[1]);
            } else if (order==2) {
                order2(str[1]);
            } else if (order==3) {
                order3(str[1]);
            }
        }
    }


    //四舍五入
    public static void order1(String number) {
        //System.out.println("四舍五入");
        Double v = Double.valueOf(number);
        System.out.printf("%." + digit + "f", v);
        System.out.println();
    }

    //舍弃小数
    public static void order2(String number) {
       // System.out.println("order2");
        for (int i = 0; i < number.length(); i++) {
            char c = number.charAt(i);
            if (c == '.') {
                System.out.print(c);
                int count = 0;
                for (int j = i + 1; j < number.length(); j++) {
                    if (count == digit) {
                        System.out.println();
                        return;
                    }
                    System.out.print(number.charAt(j));
                    count++;
                }
            }
            System.out.print(c);
        }
        System.out.println();
    }

    //四舍去六入
    public static void order3(String number) {
      //  System.out.println("order3");
        String[] numberList = number.split("");
        for (int i = 0; i < numberList.length; i++) {
            String temnum = numberList[i];
            System.out.print(temnum);
            //System.out.println("======");
            if (temnum.equals(".")) {
                int count = 0;

                for (int j = i + 1; j < numberList.length; j++) {
                    String lastnum = numberList[j];
                    if (count == digit) {
                        if (j + 1 < numberList.length && numberList[j + 1].equals(5)) {
                            if (Integer.valueOf(lastnum) % 2 == 0) {
                                System.out.println(lastnum);
                                return;
                            } else {
                                System.out.println(Integer.valueOf(lastnum) + 1);
                                return;
                            }
                        } else if (j + 1 < numberList.length && Integer.valueOf(numberList[j + 1]) > 5) {
                            System.out.println(Integer.valueOf(lastnum) + 1);
                            return;
                        } else {
                            System.out.println(lastnum);
                            return;
                        }
                    }
                    if (j == (numberList.length - 1)) {
                        System.out.println(Integer.valueOf(lastnum) + 1);
                      //  System.out.println("2222222");
                        return;
                    }
                    System.out.print(lastnum);
                    count++;

                }
                return;
            }
        }
    }

}
