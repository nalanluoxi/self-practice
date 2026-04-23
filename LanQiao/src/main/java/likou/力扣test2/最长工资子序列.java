package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：最长工资子序列
 * @Date：2025/5/18 10:52
 * @Filename：最长工资子序列
 */
public class 最长工资子序列 {
    public static void main(String[] args) {
      //  System.out.println(longestCommonSubsequence("abcde","ace"));
        System.out.println(longestCommonSubsequence("pmjghexybyrgzczy","hafcdqbgncrcbihkd"));
    }


    static int[][]dp;
    public static int longestCommonSubsequence(String text1, String text2) {
        int l1 = text1.length();
        int l2 = text2.length();
        dp=new int[l1+1][l2+1];
        return dps(text1,text2,l1,l2);
    }

    public static int dps(String text1,String text2,int len1,int len2){
        if (len1==0||len2==0){
            return 0;
        }
        if (dp[len1][len2]!=0){
            return dp[len1][len2];
        }
        int ans=0;
        if (text1.charAt(len1-1)==text2.charAt(len2-1)){
            ans=dps(text1,text2,len1-1,len2-1)+1;
        }else {
            ans=Math.max(dps(text1,text2,len1-1,len2),dps(text1,text2,len1,len2-1));
        }
        dp[len1][len2]=ans;
        return ans;
    }
}
