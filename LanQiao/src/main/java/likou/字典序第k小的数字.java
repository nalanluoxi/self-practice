package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：字典序第k小的数字
 * @Date：2025/7/9 10:37
 * @Filename：字典序第k小的数字
 */
public class 字典序第k小的数字 {
    public static void main(String[] args) {
        System.out.println(findKthNumber(13, 2));
        System.out.println(findKthNumber2(13, 2));
    }

    public static int findKthNumber(int n, int k) {
        int cur=1;
        k--;
        while (k>0){
            int step= getstep(cur,n);
            if (step<=k){
                k-=step;
                cur++;
            }else {
                cur=cur*10;
                k--;
            }
        }
        return cur;
    }
    public static int getstep(int cur,int n){
        int step=0;
        long first=cur;
        long last=cur;
        while (first<=n){
            step+=Math.min(n,last)-first+1;
            first=first*10;
            last=last*10+9;
        }
        return step;
    }

    public static int findKthNumber2(int n, int k) {
        int curr = 1;
        k--;
        while (k > 0) {
            int steps = getSteps(curr, n);
            if (steps <= k) {
                k -= steps;
                curr++;
            } else {
                curr = curr * 10;
                k--;
            }
        }
        return curr;
    }

    public static int getSteps(int curr, long n) {
        int steps = 0;
        long first = curr;
        long last = curr;
        while (first <= n) {
            steps += Math.min(last, n) - first + 1;
            first = first * 10;
            last = last * 10 + 9;
        }
        return steps;
    }


}
