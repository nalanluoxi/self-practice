package 蓝桥杯真题.决12A组;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.决12A组
 * @Project：LanQiaoBei
 * @name：冰山
 * @Date：2025/4/11 22:06
 * @Filename：冰山
 */
public class 冰山 {
    static List<Integer>list;
    static int k;
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt();
        int m=scanner.nextInt();
        k=scanner.nextInt();
        list=new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(scanner.nextInt());
        }
        for (int i = 0; i < m; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            help(x,y);
        }
    }

    public static void help(int x,int y){
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Integer temp = list.get(i);
            if (temp==-1){
                continue;
            }
            temp+=x;
            if (temp<=0){
                list.set(i,-1);
            } else if (temp<=k) {
                list.set(i,temp);
            } else if (temp>k) {
                list.set(i,k);
                temp-=k;
                while (temp>0){
                    list.add(1);
                    temp--;
                }
            }
        }
        list.add(y);

        printAll();
    }

    public static void printAll(){
        int sum=0;
        for (int i = 0; i < list.size(); i++) {
            Integer temp = list.get(i);
            if (temp!=-1){
                sum+=temp;
            }
        }
        System.out.println(sum);
    }
}
