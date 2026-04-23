package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：Test1002
 * @Date：2025/10/2 23:12
 * @Filename：Test1002
 */
public class Test1002 {


    public static void main(String[] args) {
        boolean abcced = exist(new char[][]{{'A', 'B', 'C', 'E'}, {'S', 'F', 'C', 'S'}, {'A', 'D', 'E', 'E'}}, "ABCCED");
        System.out.println(abcced);
    }

    public static boolean exist(char[][] nums, String word) {
        int n = nums.length;
        int m = nums[0].length;
        int[][]visited=new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
               boolean b= deep(i,j,0,visited,nums,word);
                if (b){
                    return b;
                }
            }
        }
        return false;
    }


    public static boolean deep(int i,int j,int index,int[][]visited,char[][] nums,String word){
        if (nums[i][j]!=word.charAt(index)){
            return false;
        }
        if (index==word.length()-1){
            return true;
        }
        visited[i][j]=1;
        int n = nums.length;
        int m = nums[0].length;
        boolean res=false;
        int[][]direction={{0,1},{0,-1},{1,0},{-1,0}};
        for (int[] d : direction) {
            int nx = i + d[0];
            int ny = j + d[1];
            if (nx>=0&&nx<n&&ny>=0&&ny<m && visited[nx][ny]==0){
                boolean b= deep(nx,ny,index+1,visited,nums,word);
                if (b){
                    res=true;
                    break;
                }
            }
        }
        visited[i][j]=0;
        return res;
    }
}
