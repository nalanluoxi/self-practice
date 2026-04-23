package luogu;

import java.util.Scanner;

public class 车站 {
    public static void main(String[] args) {
        chezhan();
    }

    public static void chezhan() {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int x = scanner.nextInt();
        if (x <= 3) {
            if (x <= 2) {
                System.out.println(a);
            }
            if (x == 3) {
                System.out.println(2 * a);
            }
            return;
        }

        Car[] cars = new Car[n + 1];
        Car[] ups = new Car[n + 1];
        cars[1] = new Car(1, 0);
        cars[2] = new Car(1, 0);
        cars[3] = new Car(2, 0);
        ups[1] = new Car(0, 0);
        ups[2] = new Car(1, 0);
        ups[3] = new Car(0, 1);


        for (int i = 4; i <= n - 1; i++) {
            cars[i] = new Car(cars[i - 1].anum + ups[i - 1].anum, cars[i - 1].unum + ups[i - 1].unum);
            ups [i] = new Car(ups[i - 1].anum + ups[i - 2].anum, ups[i - 1].unum + ups[i - 2].unum);
        }

        long u=(m-cars[n-1].anum*a)/cars[n-1].unum;

        for (int i = 1; i <= n - 1; i++) {
            cars[i].sum=cars[i].anum*a+cars[i].unum*u;
        }
        System.out.println(cars[x].sum);

    }
}

class Car {
    long anum;
    long unum;
    Long sum;

    public Car(long anum, long unum) {
        this.anum = anum;
        this.unum = unum;
    }
}
