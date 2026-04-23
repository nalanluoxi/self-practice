package luogu;

import java.util.Scanner;

public class 四平方定理 {
    public static void main(String[] args) {
        siping();
    }

    public static void siping(){
        Scanner scanner=new Scanner(System.in);
        int n = scanner.nextInt();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int k = 0; k < 9; k++) {
                    for (int l = 0; l < 9; l++) {
                        /*int sum = addPing(i, j, k, l);
                        if (sum)*/
                        if (addPing(i,j,k,l)==n){
                            System.out.println(i+" "+j+" "+k+" "+l);
                            return;
                        }
                    }
                }
            }
        }

    }

    public static int addPing(int a,int b,int c,int d){
        return (int) (Math.pow(a,2)+Math.pow(b,2)+Math.pow(c,2)+Math.pow(d,2));
    }
}
