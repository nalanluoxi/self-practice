package likou.力扣test2;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class test260911 {

    public static void main(String[] args) {
        //test01();

        //test02();


        test03();

    }


    public static void test03() {
        System.out.println("-----test03---------");
        RateUtils rateUtils = new RateUtils(5,4);

        for (int i = 0; i < 10; i++) {
            //System.out.println("------------------");

            boolean b = rateUtils.rate_limited_function();


            if (b){
                System.out.println("Time "+i +": Sucess");
            }else {
                System.out.println("Time "+i +": Failure");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("-----test03---------");
    }


    static class RateUtils {

        Map<Long, Integer> map;

        Long lastTime;

        Long t;

        Integer limit;

        public RateUtils(Integer T, Integer Limit) {
            map = new ConcurrentHashMap<>();
            lastTime = 0l;
            this.limit = Limit;
            this.t = T * 1000l;
        }

        public boolean  rate_limited_function() {

            long l2 = lastTime + t;
            long l = System.currentTimeMillis();
            //System.out.println("当前时间:"+l);
            boolean b = (lastTime + t) < l;
            //System.out.println("是需要换："+b);
            if (lastTime == 0) {
                long l1 = System.currentTimeMillis();
                lastTime = l1;
                map.put(lastTime, 1);
              //  System.out.println("0001");
                return true;
            }
            if (l2 >= l) {
                Integer i = map.get(lastTime);
                if (i < 4) {
                    map.put(lastTime, i + 1);
                //    System.out.println("002");
                    return true;
                } else {

                   // System.out.println("003");
                    return false;
                }
            } else {
                lastTime =l2;
                map.put(lastTime, 1);

                //System.out.println("004");
                return true;
            }
        }

    }


    public static void test02() {
        System.out.println("-----test02---------");

        int[] nums = {2, 7, 1, 8, 4};
        System.out.println(max_drawdown(nums));
        // 7-1=6
        System.out.println("-----test02---------");

    }

    public static int max_drawdown(int[] nums) {
        int[] dp = new int[nums.length];
        int len = nums.length;
        dp[len - 1] = 0;
        int ans = Integer.MIN_VALUE;
        for (int i = len - 2; i >= 0; i--) {
            for (int j = i + 1; j < len; j++) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], nums[i] - nums[j]);
                    ans = Math.max(ans, dp[i]);
                }
            }
        }

        return ans;
    }


    public static void test01() {
        System.out.println("-----test01---------");
        int[] nums = {5, 3, 5};
        System.out.println(find_second_largest(nums));
        System.out.println("-----test01---------");
    }

    public static Integer find_second_largest(int[] nums) {
        if (nums.length <= 2) {
            return null;
        }
        sort(nums, 0, nums.length - 1);
        int max = nums[nums.length - 1];
        int i = nums.length - 2;
        while (i >= 0 && nums[i] == max) {
            i--;
        }
        return nums[i] == max ? null : nums[i];
    }

    public static void sort(int[] nums, int left, int right) {
        if (left >= right) {
            return;
        }
        int mid = left + (right - left) / 2;
        sort(nums, left, mid);
        sort(nums, mid + 1, right);
        addTwo(nums, left, mid, right);
    }

    public static void addTwo(int[] nums, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1;
        int k = 0;
        while (i <= mid && j <= right) {
            if (nums[i] <= nums[j]) {
                temp[k++] = nums[i++];
            } else {
                temp[k++] = nums[j++];
            }
        }
        while (i <= mid) {
            temp[k++] = nums[i++];
        }
        while (j <= right) {
            temp[k++] = nums[j++];
        }

        for (int l = 0; l < temp.length; l++) {
            nums[l + left] = temp[l];
        }
    }


}
