package 多线程;


import  java.util.*;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author 纳兰洛熙
 * @Package：多线程
 * @Project：LanQiaoBei
 * @name：Test
 * @Date：2025/5/22 17:56
 * @Filename：Test
 */
public class Test {

    public static void main(String[] args) {
       /* int[][]num={
                {1,3}
        ,{2,6}
        ,{8,10}
        ,{15,18}
        };
*/
        int[][]num={
                {1,4},
                {4,5}
        };

        int[][] merge = merge(num);
        for (int[] ints : merge) {
            for (int anInt : ints) {
                System.out.print(anInt+" ");
            }
            System.out.println();
        }
    }

    public static int[][] merge(int[][] nums) {
        Arrays.sort(nums,new Comparator<>(){
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0]-o2[0];
            }
        });
        List<int[]>list=new ArrayList<>();
        list.add(new int[]{nums[0][0],nums[0][1]});
        for (int i = 1; i < nums.length; i++) {
            int[] ints = list.get(list.size() - 1);
            if (nums[i][0]>ints[1]){
                list.add(new int[]{nums[i][0],nums[i][1]});
            } else if (nums[i][0]<=ints[1]) {
                list.remove(list.size()-1);
                int[]ints1 = { Math.min(ints[0],nums[i][0]) ,Math.max(ints[1],nums[i][1])};
                list.add(ints1);
            }
        }
        return list.toArray(new int[list.size()][]);
    }
   /* public static void main(String[] args) {
        int[]nums={9,6,8,4,5,3,4,2,11};
        int[] ints = sortArray(nums);
        for (int anInt : ints) {
            System.out.println(anInt+" ");
        }
    }

    public static int[] sortArray(int[] nums) {
        sort(nums,0,nums.length-1);
        return nums;
    }

    public static void sort(int [] nums,int left ,int right){
        if(left>=right){
            return;
        }
        int mid = left+(right-left)/2;
        sort(nums,left,mid);
        sort(nums,mid+1,right);
        addTwo(nums,left,mid,right);
    }

    public static void addTwo(int[]nums ,int left ,int mid ,int right){
        int []temp=new int[right-left+1];
        int i=left,j=mid+1,k=0;
        while(i<=mid && j<=right){
            if(nums[i]<=nums[j]){
                temp[k++]=nums[i++];
            }else{
                temp[k++]=nums[j++];
            }
        }
        while(i<=mid){
            temp[k++]=nums[i++];
        }
        while(j<=right){
            temp[k++]=nums[j++];
        }
        for(int l=0;l<temp.length;l++){
            nums[left+l]=temp[l];
        }
    }*/
  /*  static ReentrantLock lock = new ReentrantLock();

    static Condition a1=lock.newCondition();
    static Condition a2=lock.newCondition();
    static Condition a3=lock.newCondition();
    static int status;
    static int num;
    public static void main(String[] args) {
        num=0;
        status=1;
        Thread t1=new Thread(()->{
            while (num<=100){
                lock.lock();
                try {
                    while (status!=1){
                        a1.await();
                    }
                    if (num>100){
                        break;
                    }
                    System.out.println("线程1："+num++);
                    status=2;
                    a2.signal();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }

        },"t1");
        Thread t2=new Thread(()->{
            while (num<=100){
                lock.lock();
                try {
                    while (status!=2){
                        a2.await();
                    }
                    if (num>100){
                        break;
                    }
                    System.out.println("线程2："+num++);
                    status=3;
                    a3.signal();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }

        },"t2");
        Thread t3=new Thread(()->{
            while (num<=100){
                lock.lock();
                try {
                    while (status!=3){
                        a3.await();
                    }
                    if (num>100){
                        break;
                    }
                    System.out.println("线程3："+num++);
                    status=1;
                    a1.signal();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        },"t3");

        t1.start();
        t2.start();
        t3.start();
    }
*/

}
