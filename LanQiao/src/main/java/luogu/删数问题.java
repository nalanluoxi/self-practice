package luogu;

import java.util.Scanner;

public class 删数问题 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String string = scanner.nextLine();
        int k = scanner.nextInt();


        int i=0;
        while (i <= string.length() - 2){
            if (k == 0) {
                //System.out.println("提前结束" + string);
                System.out.println(removeZ(string));
                return;
            }
            char c1 = string.charAt(i);
            char c2 = string.charAt(i+1);
          //  System.out.println("检索" + i);
            if (c1 > c2 && k > 0) {
               // System.out.println("remove " + string.charAt(i) + "before  k:" + k);
                k--;
                if (i==0){
                    string=string.substring(i+1);
                }else {
                    string = string.substring(0, i) + string.substring(i+1);
                }
               // System.out.println("after k:" + k + " new str" + string);
                i=0;
                //System.out.println(i);
            }else {
                i++;
            }
        }
       // System.out.println("k>0" + k);
        string = string.substring(0, string.length() - k);
        System.out.println(removeZ(string));
    }

    public static String removeZ(String string){
        while (true){
            if (string.charAt(0)=='0'&&string.length()==1){
                return "0";
            }
            if (string.charAt(0)=='0'){
                string=string.substring(1);
            }else {
                break;
            }
        }
        return string;
    }
}
