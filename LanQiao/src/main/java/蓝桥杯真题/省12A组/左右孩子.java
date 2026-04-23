package 蓝桥杯真题.省12A组;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.省12A组
 * @Project：LanQiaoBei
 * @name：左右孩子
 * @Date：2025/4/11 11:27
 * @Filename：左右孩子
 */
public class 左右孩子 {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        int n = scanner.nextInt();
        int[] arr=new int[n-1];
        for (int i = 0; i < arr.length; i++) {
            arr[i]=scanner.nextInt();
        }
        help(arr);
    }

    public static void help(int[] arr){
        int len = arr.length+1;
        List<Node> list=new ArrayList<>();
        for (int i = 0; i < len; i++) {
            Node node = new Node(i+1);
            list.add(node);
        }

        for (int i = 0; i < arr.length; i++) {
            int index = i + 1;
            int parent = arr[i];
            list.get(parent-1).child.add(list.get(index));
        }

        int count=0;
        int nowindex=0;
        while (true){
            Node node = list.get(nowindex);
            int temmin=Integer.MAX_VALUE;
            int tempindex=nowindex;
            if (node.child.size()==0){
                System.out.println(count);
                return;
            }
            for (int i = 0; i < node.child.size(); i++) {
                Node childn = node.child.get(i);
                if (childn.child.size()==0){
                    count++;
                    continue;
                }
                if (childn.child.size()<temmin){
                    temmin=childn.child.size();
                    tempindex=i;
                }
            }
            if (temmin==Integer.MAX_VALUE){
                System.out.println(count);
                return;
            }
            nowindex=node.child.get(tempindex).val-1;
            count++;
        }
    }

    public static class Node{
        int val;
        List<Node> child;
        public Node(int val){
            this.val=val;
            child=new ArrayList<>();
        }
    }
}
