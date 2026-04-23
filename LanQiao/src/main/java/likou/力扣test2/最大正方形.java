package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：最大正方形
 * @Date：2025/6/8 18:31
 * @Filename：最大正方形
 */
public class 最大正方形 {

    public static void main(String[] args) {
        char[][]arr=new char[][]{
                {'1','0','1','0','0'},
                {'1','0','1','1','1'},
                {'1','1','1','1','1'},
                {'1','0','0','1','0'}
        };
        System.out.println(maximalSquare(arr));
    }
    public static int maximalSquare(char[][] matrix) {
        int n=matrix.length;
        int m=matrix[0].length;
        int[][]dp=new int[n][m];
        int ans=0;
        for(int i=0;i<n;i++){
            if(matrix[i][0]=='1'){
                dp[i][0]=1;
            }
            ans=Math.max(ans,dp[i][0]);
        }
        for(int i=0;i<m;i++){
            if(matrix[0][i]=='1'){
                dp[0][i]=1;
            }
            ans=Math.max(ans,dp[0][i]);
        }
        for(int i=1;i<n;i++){
            for(int j=1;j<m;j++){
                if(matrix[i][j]=='0'){
                    dp[i][j]=0;
                }else{
                    dp[i][j]=Math.min(dp[i-1][j-1],Math.min(dp[i-1][j],dp[i][j-1]))+1;
                }
                ans=Math.max(ans,dp[i][j]);
            }
        }
        return ans*ans;
    }
}
