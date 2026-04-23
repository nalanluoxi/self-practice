package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：解码方法2
 * @Date：2025/7/1 20:58
 * @Filename：解码方法2
 */
public class 解码方法2 {

    public static void main(String[] args) {
        System.out.println(numDecodings("12"));
    }
    public static int numDecodings(String s) {
        if (s.length()<=0||s.charAt(0)=='0'){
            return 0;
        }
        int len = s.length();
        int[]dp=new int[len+1];
        dp[0]=1;
        for (int i = 1; i <= len; i++) {
            if (s.charAt(i-1)!='0'){
                dp[i]=dp[i-1];
            }else {
                dp[i]=0;
            }
            if (i>1&&isOk(s.substring(i-2,i))){
                dp[i]+=dp[i-2];
            }
        }
        return dp[len];
    }

    public static boolean isOk(String s){
        if (s.length()<=0||s.length()>2){
            return false;
        }
        if (s.charAt(0)=='0'){
            return false;
        }
        Integer i = Integer.valueOf(s);
        return i>=1&&i<=26;
    }
}

