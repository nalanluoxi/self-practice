package 多线程;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：多线程
 * @Project：LanQiaoBei
 * @name：多线程见的比较
 * @Date：2025/4/28 15:56
 * @Filename：多线程见的比较
 */
public class 多线程见的比较 {
    public static void main(String[] args) {

    }

    public static class MyThread extends Thread{
        static List<Integer>list;
        static int max;
        @Override
        public void run() {
            ArrayList<Integer> temp=new ArrayList<>();
            int tempmax=Integer.MIN_VALUE;
            int all=0;
            while (true){
                synchronized (MyThread.class){
                    if (list.size()==0){
                        System.out.println(getName()+"共抽中"+temp+"其中最大的是"+tempmax+"共计："+all);
                        max=Math.max(max,all);
                        return;
                    }else {
                        Collections.shuffle(list);
                        int now = list.remove(0);
                        temp.add(now);
                    }
                }
            }
        }
    }
}
