package luogu;

import java.util.Scanner;

public class 铺地毯 {
    public static void main(String[] args) {
        puditan();
    }
    public static int[][] tan=new int[10001][4];
    public static void puditan(){
        Scanner scanner =new Scanner(System.in);
        int n= scanner.nextInt();

        for (int i = 1; i <= n; i++) {
            tan[i][0]= scanner.nextInt();
            tan[i][1]= scanner.nextInt();
            tan[i][2]=tan[i][0]+ scanner.nextInt();
            tan[i][3]=tan[i][1]+ scanner.nextInt();
        }

        int x= scanner.nextInt();
        int y= scanner.nextInt();
        for (int i = n; i >=0; i--) {
            if (tan[i][2]>=x&& tan[i][3]>=y&& tan[i][0]<=x&& tan[i][1]<=y){
                System.out.println(i);
                return;
            }
        }
        System.out.println(-1);
        
    }
}
