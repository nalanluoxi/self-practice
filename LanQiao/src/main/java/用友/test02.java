package 用友;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：用友
 * @Project：LanQiaoBei
 * @name：test02
 * @Date：2025/8/18 19:25
 * @Filename：test02
 */
public class test02 {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] split = scanner.nextLine().split(" ");
        int n=Integer.valueOf(split[0]);
        int k=Integer.valueOf(split[1]);
        /*int [] arr=new int[n];
        int [] brr=new int[n];*/
        String[]arr=new String[n];
        for (int i = 0; i < n; i++) {
            String string = scanner.nextLine();
            arr[i]=string;
        }
        help(arr,k);
    }

    public static void help(String[]arr,int k){
        PriorityQueue<int[]> queue=new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] a1, int[] a2) {
                return a1[1]-a2[1];
            }
        });
        for (int i = 0; i < arr.length; i++) {
            String[] split = arr[i].split(" ");
            queue.add(new int[]{i,Integer.valueOf(split[0]),Integer.valueOf(split[1])});
        }
        PriorityQueue<Integer> kqu=new PriorityQueue<>();
        for (int i = 0; i < k; i++) {
            kqu.add(i+1);
        }
        while (!queue.isEmpty()){
            int[] poll = queue.poll();
           // kqu.p
        }

    }
}
