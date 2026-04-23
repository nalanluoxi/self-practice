package pactice;

import java.util.HashSet;
import java.util.Set;

public class Numbear {
    public static void main(String[] args) {
        String s="a1b2c3";
        number(s);
    }

    public static String number(String s){
        Set<String> num=new HashSet<>();
        num.add("0");
        num.add("1");
        num.add("2");
        num.add("3");
        num.add("4");
        num.add("5");
        num.add("6");
        num.add("7");
        num.add("8");
        num.add("9");
        String[] ns = new String[s.length()];
        for (int i = 0; i < s.length(); i++) {
            ns[i] = String.valueOf(s.charAt(i));
        }
        for (int i = 0; i < s.length(); i++) {
            if (num.contains(ns[i])){
                ns[i]="number";
            }
        }
        s=String.join("",ns);
        return s;

    }
}
