package PTA;


import java.util.*;
public class 写数 {
    public static void main(String args[]){
        xieshu();
    }


    static Scanner scanner=new Scanner(System.in);
    public static void xieshu(){
        String[] pin={"lin","yi","er","san","si","wu","liu","qi","ba","jiu"};
        String n=scanner.nextLine();
        String[] s = n.split("");
        int count=0;
        for(int i =0;i<s.length;i++){
            count+=Integer.valueOf(s[i]);
        }
        String[] s1 = String.valueOf(count).split("");
        for(int i=0;i<s1.length;i++){
            System.out.printf(pin[Integer.valueOf(s1[i])]);
            if(i!=s1.length-1){
                System.out.printf(" ");
            }
        }


    }
}
