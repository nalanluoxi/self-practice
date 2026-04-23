package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：子数组最小值之和
 * @Date：2025/2/24 20:10
 * @Filename：子数组最小值之和
 */
public class 子数组最小值之和 {
    public static void main(String[] args) {
        int[] ints = {11, 81, 94, 43, 3};
        //int[] ints = {3, 1, 2, 4};
        int i = sumSubarrayMins(ints);
        System.out.println(i);
        /*List<List<Integer>> all = getAll(new int[]{1, 2, 3});
        for (List<Integer> integers : all) {
            System.out.println(integers);
        }*/
    }

    static int mod = (int) (1e9 + 7);
    static int[] stack;
    static int r;

    public static int sumSubarrayMins(int[] arr) {
        int len = arr.length;
        stack = new int[len];
        r = 0;
        long sum = 0;
        for (int i = 0; i < arr.length; i++) {
            while (r > 0 && arr[i] <= arr[stack[r - 1]]) {
                Integer lastindex = stack[--r];
                int left = r == 0 ? -1 : stack[r - 1];
                sum=(sum+(long) arr[lastindex]*(i-lastindex)*(lastindex-left)%mod)%mod;
            }
            stack[r++] = i;
        }
        while (r>0){
            int lastindex = stack[--r];
            int left = r == 0? -1 : stack[r - 1];
            sum=(sum +(long) arr[lastindex]*(len-lastindex)*(lastindex-left)%mod)%mod;
        }
        return (int) sum;
    }




   /* static int mod = (int) (1e9 + 7);
    public static int sumSubarrayMins(int[] arr) {
       // Arrays.sort(arr);
        List<List<Integer>> all = getAll(arr);
        int sum = 0;
        for (List<Integer> integers : all) {
            int min = integers.get(0);
            for (Integer integer : integers) {
                min = Math.min(min, integer);
            }
            min %= mod;
            sum += min;
            sum %= mod;
        }
        return sum;
    }


    public static List<List<Integer>> getAll(int[] arr) {
        ans = new ArrayList<>();
        tans = new ArrayList<>();
        for (int i = 1; i <= arr.length; i++) {
            dfs(arr, i, 0);
        }
        return ans;
    }

    static List<List<Integer>> ans;
    static List<Integer> tans;

    public static void dfs(int[] arr, int n, int index) {
        for (int i = index; i < arr.length; i++) {
            if (i+n>arr.length)return;
            for (int j = i; j < n+i; j++) {
                tans.add(arr[j]);
            }
            ans.add(new ArrayList<>(tans));
            for (int j = i; j < n+i; j++) {
                tans.remove(tans.size() - 1);
            }
        }
    }*/
}
