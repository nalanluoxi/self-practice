package likou.贪心;

/**
 * @Author 纳兰洛熙
 * @Package：likou.贪心
 * @Project：LanQiaoBei
 * @name：加油站
 * @Date：2025/3/13 20:27
 * @Filename：加油站
 */
public class 加油站 {
    public static void main(String[] args) {
       // System.out.println(canCompleteCircuit(new int[]{1, 2, 3, 4, 5}, new int[]{3, 4, 5, 1, 2}));
        System.out.println(canCompleteCircuit(new int[]{2,3,4}, new int[]{3,4,3}));
    }
    public static int canCompleteCircuit(int[] gas, int[] cost) {
        int sum =0;
        int allsum=0;
        int min = Integer.MAX_VALUE;
        int ans=0;
        for (int i = 0; i < gas.length; i++) {
            int rest = gas[i]-cost[i];
            sum+=rest;
            allsum+=rest;
            min = Math.min(min,sum);
            if (sum<0){
                ans=i+1;
                sum=0;
            }
        }
        if (allsum<0){
            return -1;
        }
        return ans;
    }

 /*   public static int canCompleteCircuit(int[] gas, int[] cost) {
        int[] rest = new int[gas.length];
        int sum = 0;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < gas.length; i++) {
            rest[i] = gas[i] - cost[i];
            sum += rest[i];
            min = Math.min(min, sum);
        }
        if (sum < 0) {
            return -1;
        }
        if (min >= 0) {
            return 0;
        }
        for (int i = gas.length-1; i >=0; i--) {
            min+=rest[i];
            if (min>=0){
                return i;
            }
        }
        return -1;
    }*/
}
