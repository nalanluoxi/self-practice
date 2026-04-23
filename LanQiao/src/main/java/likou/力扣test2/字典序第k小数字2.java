package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：字典序第k小数字2
 * @Date：2025/7/15 10:49
 * @Filename：字典序第k小数字2
 */
public class 字典序第k小数字2 {
    public static void main(String[] args) {

    }

    public static int findKthNumber(int n, int k) {
        int cur=1;
        k--;
        while (k>0){
            int step = getStep(cur, n);
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

    public static int getStep(int cur,int n){
        int step=0;
        int first=cur;
        int last=cur;
        while (first<=n){
            step+=Math.min(last,n)-first+1;
            first=first*10;
            last=last*10+9;
        }
        return step;
    }


}
