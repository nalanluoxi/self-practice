package example.java2;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2
 * @Project：LanQiaoBei
 * @name：ArrayUtils
 * @Date：2025/7/10 17:01
 * @Filename：ArrayUtils
 */
public class ArrayUtils {

    static ReentrantLock lock = new ReentrantLock();
    static Condition a1 = lock.newCondition();
    static Condition a2 = lock.newCondition();
    static volatile int status = 1;

    static int num = 1;

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            while (num <= 100) {
                lock.lock();
                try {
                    while (status != 1) {
                        a1.await();
                    }
                    System.out.println("线程1:    " + num);
                    num++;
                    status = 2;
                    a2.signal();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            while (num <= 100) {
                lock.lock();
                try {
                    while (status != 2) {
                        a2.await();
                    }
                    System.out.println("线程2:    " + num);
                    num++;
                    status = 1;
                    a1.signal();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        });
        t1.start();
        t2.start();


    }

    public static int[] findMinMax(int[] nums) {
        if (nums == null || nums.length == 0) {
            return nums;
        }
        partition(nums, 0, nums.length - 1);
        return new int[]{nums[0], nums[nums.length - 1]};
    }

    public static void partition(int[] nums, int left, int right) {
        if (left >= right) {
            return;
        }
        int mid = left + (right - left) / 2;
        partition(nums, left, mid);
        partition(nums, mid + 1, right);
        addTwo(nums, left, mid, right);
    }

    public static void addTwo(int[] nums, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1;
        int k = 0;
        while (i <= mid && j <= right) {
            if (nums[i] < nums[j]) {
                temp[k++] = nums[i++];
            } else {
                temp[k++] = nums[j++];
            }
        }
        while (i <= mid) {
            temp[k++] = nums[i++];
        }
        while ((j <= right)) {
            temp[k++] = nums[j++];
        }
        k = 0;
        for (int l = 0; l < temp.length; l++) {
            nums[l + left] = temp[l];
        }
    }
}
