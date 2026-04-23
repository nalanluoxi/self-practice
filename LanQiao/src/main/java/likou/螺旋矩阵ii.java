package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：螺旋矩阵ii
 * @Date：2025/7/7 10:27
 * @Filename：螺旋矩阵ii
 */
public class 螺旋矩阵ii {


    public static void main(String[] args) {
        int[][] ints = generateMatrix(3);
        for (int[] anInt : ints) {
            for (int i : anInt) {
                System.out.print(i+" ");
            }
            System.out.println();
        }
    }
    static int[][]ans;

    public static int[][] generateMatrix(int n) {
        ans=new int[n][n];
        int x=0,y=0;
        int cur=1;
        int[][]direct={{1,0},{0,1},{-1,0},{0,-1}};
        int d=0;
        while (cur<=n*n){
            ans[y][x]=cur;
            cur++;
            int nx = x + direct[d][0];
            int ny = y + direct[d][1];
            if (nx<0||nx>=n||ny<0||ny>=n||ans[ny][nx]!=0){
                d=(d+1)%4;
            }
            x=x+direct[d][0];
            y=y+direct[d][1];
        }
        return ans;
    }

}
