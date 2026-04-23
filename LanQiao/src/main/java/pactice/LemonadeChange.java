package pactice;

public class LemonadeChange {

    public static void main(String[] args) {
        int []bills={5,5,5,10,20};
      //  System.out.println(bills[0]==0);
        boolean b = lemonadeChange(bills);
       System.out.println(b);
    }

    public static boolean lemonadeChange(int[] bills) {
        int five=0;
        int ten=0;
        int dou=0;
        for (int i = 0; i < bills.length; i++) {
            if (bills[i]==5){
                five++;
            } else if (bills[i]==10) {
                ten++;
                five--;
            } else if (bills[i]==20) {
                if (ten>0){
                    ten--;
                    five--;
                    dou++;
                }else {
                    five-=3;
                    dou++;
                }
            }
            if (five<0||ten<0||dou<0){
                return false;
            }


        }
        return true;
    }
}
