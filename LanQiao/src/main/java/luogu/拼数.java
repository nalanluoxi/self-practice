package luogu;

import java.util.*;

public class 拼数 {
    public static void main(String[] args) {
        pin();
    }

    public static void pin() {
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine());
        //int n = scanner.nextInt();
        //scanner.nextLine();
        String[] arr = new String[n];
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.next();
        }
        Arrays.sort(arr, (o1, o2) -> {
            return (o1 + o2).compareTo(o2 + o1);
        });
       /* Arrays.sort(arr,(x,y)->{
            return (x+y).compareTo(y+x);
        });*/



        for (int i = n - 1; i >= 0; i--) {
            System.out.print(arr[i]);
        }

    }


}
