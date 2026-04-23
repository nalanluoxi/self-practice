package 蓝桥杯真题.省12A组;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.省12A组
 * @Project：LanQiaoBei
 * @name：回路计数
 * @Date：2025/4/11 9:39
 * @Filename：回路计数
 */
public class 回路计数 {
    public static void main(String[] args) {
        //System.out.println(isZhi(3,2));
        /*int n=21;
        dp=new int[n+1];
        help(1);
        System.out.println(count);*/
        help2();
        System.out.println(ans);
    }

  /*  static long count=0;
    static int[] dp;
    public static void help(int n){
        for (int i=1;i<=21;i++){
            if (dp[i-1]==0 && isZhi(n,i)){
                dp[i-1]=1;
                help(i);
                dp[i-1]=0;
                if(i==21){
                    count++;
                }
            }
        }
    }
    public static boolean isZhi(int a,int b){
        return getYue(a,b)==1;
    }

    public static int getYue(int a,int b){
        return b==0?a:getYue(b,a%b);
    }

*/
    static int[] v=new int[21];
    static int ans=0;
    public static void help2() {
        v[0]=1;
        search(1);
    }
    public static int gcd(int a,int b){
        if (b == 0) {
            return a;
        } else {
            return gcd(b, a % b);
        }
    }
    public static void search(int start){
        for(int i=1;i<=21;i++){
            if(v[i-1]==0&&gcd(i,start)==1){
                v[i-1]=1;
                search(i);
                v[i-1]=0;
                if(i==21){
                    ans++;
                }
            }
        }
    }
}
