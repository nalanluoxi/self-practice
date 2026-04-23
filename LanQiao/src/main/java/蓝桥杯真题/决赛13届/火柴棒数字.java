package 蓝桥杯真题.决赛13届;

import java.math.BigInteger;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.决赛13届
 * @Project：LanQiaoBei
 * @name：火柴棒数字
 * @Date：2025/4/8 11:09
 * @Filename：火柴棒数字
 */
public class 火柴棒数字 {
    public static void main(String[] args) {
        /**
         *  9 :10 60
         *  8 10  70
         *  7 10 30
         *  6 10 60
         *  5 10 50
         *  4 7 28
         */
        int i = 60 + 70 + 30 + 60 + 50 + 28;
        System.out.println(i);
        String s = "";
        for (int j = 0; j < 10; j++) {
            s += "9";
        }
        for (int j = 0; j < 10; j++) {
            s += "8";
        }
        for (int j = 0; j < 10; j++) {
            s += "7";
        }
        for (int j = 0; j < 10; j++) {
            s += "6";
        }
        for (int j = 0; j < 10; j++) {
            s += "5";
        }
        for (int j = 0; j < 7; j++) {
            s += "4";
        }
        System.out.println(s);
        System.out.println(s.length());
      /*  BigInteger bigInteger = new BigInteger("999999999888888888877777777776666666666555555555544444441");
        System.out.println(bigInteger.toString());
*/
       // System.out.println("999999999988888888887777777777666666666655555555554444444");
       // System.out.println("9999999999888888888877777777776666666666555555555544444441");
        String s2="";
        for (int j = 0; j < 10; j++) {
            s2+="9";
        }
        for (int j = 0; j < 10; j++) {
            s2+="7";
        }

        for (int j = 0; j < 10; j++) {
            s2+="5";
        }

        for (int j = 0; j < 10; j++) {
            s2+="4";
        }
        for (int j = 0; j < 10; j++) {
            s2+="3";
        }

        for (int j = 0; j < 10; j++) {
            s2+="2";
        }

        for (int j = 0; j < 10; j++) {
            s2+="1";
        }

        System.out.println(s2);
        System.out.println(s2.length());
        System.out.println("9999999999777777777755555555554444444444333333333322222222221111111111");

    }
}
