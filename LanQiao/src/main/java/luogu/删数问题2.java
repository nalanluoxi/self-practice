package luogu;

import java.util.Scanner;

public class 删数问题2 {
  //  public static void main(String[] args) {
 /*       String  string="175438";
        System.out.println(getMinIndex(string,0));
    }*/

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String string = scanner.nextLine();
        int k = scanner.nextInt();
        String res="";
        int index=0;
        int len = string.length();
        System.out.println("string "+string);
        System.out.println("res :"+res);
        while (true){
            System.out.println(string);
            System.out.println(index);
            int minIndex = getMinIndex(string, index);
            System.out.println("minIndex:"+minIndex);
            System.out.println("len-k-1: "+(len-k-1));
            System.out.println("len-minIndex: "+(len-minIndex));
            Thread.sleep(200000);
            if (len-k-1<=len-minIndex){
                System.out.println("加数");
                res+=string.charAt(minIndex);
                index=minIndex+1;
                k--;
                len--;
                System.out.println(res);
            }
            if (k==0){
                break;
            }
        }
      //  System.out.println(res.toString());

    }

    public static int getMinIndex(String  string,int startIndex){
        Integer min=Integer.valueOf(string.charAt(0));
        int minIndex=0;
        for (int i = startIndex; i < string.length(); i++) {
            if (Integer.valueOf(string.charAt(i))<min){
                min=Integer.valueOf(string.charAt(i));
                minIndex=i;
            }
        }
        System.out.println("mindex    "+minIndex);
        return minIndex;
    }
}
