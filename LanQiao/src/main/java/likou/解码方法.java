package likou;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：解码方法
 * @Date：2025/1/26 16:14
 * @Filename：解码方法
 */
public class 解码方法 {
    public static void main(String[] args) {
        int i = numDecodings("12");
       /* for (int t : dp) {
            System.out.println(t);
        }*/
        System.out.println(i);
    }


    /*    public static int f(int n){
            dp[n-1]=1;
            for (int i = n-1; i >=0 ; i--) {
                String temnum="";
                for (int j = 0; j <2&&(i-j)>=0; j++) {
                    temnum=chars[i-j]+temnum;
                    if (j==0&&Integer.parseInt(temnum)>0){
                        dp[i]+=1;
                    }
                    if (j==1&&Integer.parseInt(temnum)<=26&&Integer.parseInt(temnum)>=10){
                        dp[i-1]+=1;
                    }
                    //System.out.println(temnum);

                }
                System.out.println("dp["+i+"]="+dp[i]);
            }
            int tem=0;
            for (int i = dp.length-1; i >0; i--) {
                dp[i-1]=dp[i-1]*dp[i];
            }
            return dp[0];
        }*/
    public static int numDecodings(String s) {
        chars = s.toCharArray();
        dp = new int[chars.length];
        Arrays.fill(dp, -1);
        //return f(0);
        return f2();
    }

    static char[] chars;
    static int[] dp;

    public static int f(int n) {
        if (n == chars.length) {
            return 1;
        }
        if (chars[n] == '0') {
            return 0;
        }
        if (dp[n] != -1) {
            return dp[n];
        }
        int ans = 0;
        ans += f(n + 1);
        if (n + 1 < chars.length) {
            int temnum = (chars[n] - '0') * 10 + (chars[n + 1] - '0');
            if (temnum <= 26 && temnum >= 10) {
                ans += f(n + 2);
            }
        }
        dp[n] = ans;
        return ans;
    }

    public static int f2(){
        int n=chars.length;
        dp=new int[n+1];
        dp[n]=1;
        for (int i =n-1; i >=0 ; i--) {
            if (chars[i]=='0'){
                dp[i]=0;
            }else {
                dp[i]=dp[i+1];
                if (i+1<n){
                    int temp=(chars[i]-'0')*10+(chars[i+1]-'0');
                    if (temp<=26&&temp>=10){
                        dp[i]+=dp[i+2];
                    }
                }

            }
        }
        return dp[0];
    }

}
