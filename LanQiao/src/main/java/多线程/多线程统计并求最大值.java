package 多线程;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：多线程
 * @Project：LanQiaoBei
 * @name：多线程统计并求最大值
 * @Date：2025/4/28 15:37
 * @Filename：多线程统计并求最大值
 */
public class 多线程统计并求最大值 {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> list = new ArrayList<>();
        Collections.addAll(list,10,5,20,50,100,200,500,800,2,80,300,700);
        MyThread thread1=new MyThread(list);
        MyThread thread2=new MyThread(list);

        thread1.setName("小明");
        thread2.setName("小红");
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("统计完毕");
    }

    public static class MyThread extends Thread{
        List<Integer>list;
        List<Integer>MyList;
        int all;
        int max;

        public MyThread(List<Integer> list) {
            this.list = list;
        }

        @Override
        public void run() {
            MyList=new ArrayList<>();
            all=0;
            max=Integer.MIN_VALUE;
            while (list.size()>0){
                synchronized (MyThread.class){
                    if (list.size()>0){
                        Collections.shuffle(list);
                        Integer remove = list.remove(0);
                        MyList.add(remove);
                        all+=remove;
                        max=Math.max(max,remove);
                    }
                }
            }
            Collections.sort(MyList);
            System.out.println("用户:"+Thread.currentThread().getName()+"统计完毕，统计的结果为"+MyList.toString().replace("[", "").replace("]","")+"最大值为"+max+"总和为"+all);
        }
    }
}
