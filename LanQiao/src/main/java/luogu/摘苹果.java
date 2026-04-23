package luogu;


import java.util.Scanner;

public class 摘苹果 {
    public static void main(String[] args) {
        pinggu();
    }
    public static void pinggu(){
        Scanner scanner=new Scanner(System.in);
        int count=0;
        String nums= scanner.nextLine();
        int heigh= scanner.nextInt()+30;
        for (String n:nums.split(" "))
            if (Integer.valueOf(n)<=heigh) count++;

        System.out.println(count);
    }
}
