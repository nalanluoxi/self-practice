package PTA;
import java.util.*;

public class 猜想 {
    public static void main(String[] args) {
        caixiang();
    }
    static Scanner scanner=new Scanner(System.in);
    public static void caixiang(){
        int n=scanner.nextInt();
        int count=0;
        while(n!=1){
            if(n%2==0){
                n=n/2;
                count++;
            }else{
                n=n*3+1;
                n=n/2;
                count++;
            }
        }
        System.out.println(count);
    }
}
