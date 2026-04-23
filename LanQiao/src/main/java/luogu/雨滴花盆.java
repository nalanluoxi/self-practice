package luogu;

import java.io.*;
import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：luogu
 * @Project：LanQiaoBei
 * @name：雨滴花盆
 * @Date：2025/3/6 21:45
 * @Filename：雨滴花盆
 */
public class 雨滴花盆 {

    static int n;
    static int t;
    static int[][]arr;//=new int[100005][2];
    public static void main(String[] args) throws IOException {
        Scanner scanner=new Scanner(System.in);
        n = scanner.nextInt();
        t = scanner.nextInt();
        arr=new int[n][2];
       // System.out.println("n="+n+" t="+t);
        for (int i = 0; i < n; i++) {
            scanner.nextLine();
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            arr[i][0]=x;
            arr[i][1]=y;
        }
        int ans=complete();
        System.out.println(ans);

    }

    static Deque<Integer> maxstack;
    static Deque<Integer> minstack;

    private static int complete() {
        maxstack=new LinkedList<>();
        minstack=new LinkedList<>();
        Arrays.sort(arr,0,n,(a,b)->(a[0]-b[0]));
       /* for (int i = 0; i < n; i++) {
            System.out.println("x:"+arr[i][0]+"  y:"+arr[i][1]);
        }*/
        int ans=Integer.MAX_VALUE;
        for (int i = 0,j=0; i < n; i++) {
            while (!isOk()&&j<n){
                add(j++);
            }
            if (isOk()){
                ans=Math.min(ans,Math.abs(arr[maxstack.peekFirst()][0]-arr[minstack.peekFirst()][0]));
               // System.out.println(ans);
            }
            remove(i);
        }
        return ans;
    }

    public static boolean isOk(){
        int max=maxstack.isEmpty()?0:arr[maxstack.peekFirst()][1];
        int min=minstack.isEmpty()?0:arr[minstack.peekFirst()][1];
        return max-min>=t;
    }

    public static void add(int index){
        while (!maxstack.isEmpty()&&arr[index][1] >= arr[maxstack.peekLast()][1] ){
            maxstack.pollLast();
        }
        maxstack.offerLast(index);
        while (!minstack.isEmpty()&&arr[index][1]<=arr[minstack.peekLast()][1]){
            minstack.pollLast();
        }
        minstack.offerLast(index);
    }

    public static void remove(int index){
        while (!maxstack.isEmpty()&&maxstack.peekFirst()<=index){
            maxstack.pollFirst();
        }
        while (!minstack.isEmpty()&&minstack.peekFirst()<=index){
            minstack.pollFirst();
        }
    }

}
