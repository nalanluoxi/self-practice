package luogu;

import java.util.Scanner;

public class 拼数2 {
    public static void main(String[] args) {
        pin();
    }

    public static void pin(){
        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt();
        scanner.nextLine();
        String all = scanner.nextLine();
        String[] str = all.split(" ");
        String res="";
        for (int j = 0; j < n; j++) {
            String  temMax="";
            int temMaxindex=-1;
            for (int i = 0; i < n; i++) {
                String string = str[i];
                int index = compare(temMax, string);
                if (index==2){
                    temMax=string;
                    temMaxindex=i;
                }
            }
            res=res+temMax;
            str[temMaxindex]="";
        }
        System.out.println(res);


    }

    public static int compare(String str1,String str2){
        if (str1.equals("")){
            return 2;
        }else if (str2.equals("")){
            return 1;
        }
        long num1 = getfirst(Integer.parseInt(str1));
        long num2 = getfirst(Integer.parseInt(str2));


        if (num1>num2){
            return 1;
        } else if (num1<num2) {
            return 2;
        }else {
            return compare(str1.substring(1),str2.substring(1));
        }
    }

    public static int getfirst(int num){
        int tem=num%10;
        while (true){
            if (num/10==0){
                return tem;
            }
            num=num/10;
            tem=num%10;
        }
    }




}