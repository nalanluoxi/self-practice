package likou.动态规划;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：夏季特惠
 * @Date：2025/6/24 15:35
 * @Filename：夏季特惠
 */
public class 夏季特惠 {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        int N=scanner.nextInt();//商品数量
        int X=scanner.nextInt();//总钱数
        int[] m=new int[N];
        int[] mn=new int[N];
        int[]w=new int[N];
        for (int i = 0; i < N; i++) {
            m[i]=scanner.nextInt();//原来价格
            mn[i]=scanner.nextInt();//现在价格
            w[i]=scanner.nextInt();//价值
        }
        int i = teHui(N, X, m, mn,w);
        System.out.println(i);
    }
  /*    4 100
      100 73 60
      100 89 35
      30 21 30
      10 8 10   */
    public static int teHui(int N,int X,int[]m,int[]mn,int[]w){
        List<Integer> hua=new ArrayList<>();
        List<Integer> kuai=new ArrayList<>();
        int ans=0;
        for (int i = 0; i < N; i++) {
            int t=m[i]-mn[i]-mn[i];
            if (t>=0){
                X+=t;
                ans+=w[i];
            }else {
                hua.add(-t);
                kuai.add(w[i]);
            }
        }
        int [][]dp=new int[hua.size()+1][X+1];
        for (int i = 1; i < dp.length; i++) {
            for (int j = 0; j <= X; j++) {
                dp[i][j]=dp[i-1][j];
                if (j>=hua.get(i-1)){
                    dp[i][j]=Math.max(dp[i][j],dp[i-1][j-hua.get(i-1)]+kuai.get(i-1));
                }
            }
        }
        ans+=dp[hua.size()][X];
        return ans;
    }
}
