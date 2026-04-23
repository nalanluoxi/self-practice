package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：回文子串
 * @Date：2025/5/13 19:59
 * @Filename：回文子串
 */
public class 回文子串 {
    public static void main(String[] args) {
        String s = "aaaaa";
        String as = "abc";
        System.out.println(countSubstrings(s));
    }
   static boolean[][]dp;
   static int ans;
   static char[] list;
    public static int countSubstrings(String s) {
        int n=s.length();
        dp = new boolean[n][n];
        ans=0;
        list=s.toCharArray();
        for (int i = n-1; i >=0; i--) {
            for (int j = i; j < n; j++) {
                if (list[i]==list[j]&&(j-i<=2||dp[i+1][j-1])){
                    dp[i][j]=true;
                    ans++;
                }
            }
        }
        return ans;
    }



 /*   static int ans;
    public static int countSubstrings(String s) {
        ans=0;
        dfs(s);
        return ans;
    }
    public static void dfs(String string){
        for (int i = 0; i < string.length(); i++) {
            for (int j = i; j < string.length(); j++) {
                String substring = string.substring(i, j+1);
                if(isHui(substring)){
                    ans++;
                }
            }
        }
    }


    public static boolean isHui(String str){
        StringBuffer sb = new StringBuffer(str);
        sb.reverse();
        return sb.toString().equals(str);
    }*/

}
