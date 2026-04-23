package ZHenTi;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JieCheng {
    public static void main(String[] args) {
        jiecheng();
    }

    public static void jiecheng(){
        Scanner scanner=new Scanner(System.in);
        //int len=scanner.nextInt();
        List<Integer> alist=new ArrayList<>();
        /*for (int i = 0; i < len; i++) {
            alist.add(scanner.nextInt());
        }*/
        int len=3;
        alist.add(2);
        alist.add(2);
        alist.add(2);

        System.out.println(alist);
        int sum=0;
        for (int i = 0; i < alist.size(); i++) {
            int temp = jie(alist.get(i));
            sum+=temp;
        }

        System.out.println(sum);
        for (int i = 2; i <Math.sqrt(sum); i++) {
            if (sum%jie(i)==0&&sum%jie(i+1)!=0){
                System.out.println(i);
                return;
            }
        }



    }



 /*   public static int and(){
        int sum=0;


    }*/
    public static int jie(int n){
        int j=1;
        for (int i = 1; i <= n; i++) {
            j*=i;
        }
        return j;
    }
}
