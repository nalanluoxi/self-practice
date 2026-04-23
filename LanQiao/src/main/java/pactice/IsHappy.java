package pactice;

import java.util.HashSet;
import java.util.Set;

public class IsHappy {
    public static void main(String[] args) {
        int n=19;
        boolean b=isHappy(n);
        System.out.println(b);

    }

    public static boolean isHappy(int n) {
        //int sum=0;
        Set<Integer>nums=new HashSet<>();
        while (n!=1&& !nums.contains(n)){
            nums.add(n); n=getadd(n);
        }
       // System.out.println("true");
        return n==1;
    }


    public static int getadd(int n){
        int next=0;
        while (n>0){
            int tem=n%10;
            next+=tem*tem;
            n=n/10;
        }

        return next;

    }
}
