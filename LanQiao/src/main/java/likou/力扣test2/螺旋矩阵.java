package likou.力扣test2;

import 蓝桥杯真题.省13A组.因数平方和;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：螺旋矩阵
 * @Date：2025/7/16 9:39
 * @Filename：螺旋矩阵
 */
public class 螺旋矩阵 {
    public static void main(String[] args) {
        int[][] ints = generateMatrix(3);
        for (int[] anInt : ints) {
            for (int i : anInt) {
                System.out.print(i+" ");
            }
            System.out.println();
        }
    }

    public static int[][] generateMatrix(int n) {
        int[][]ans=new int[n][n];
        int[][]dir ={{1,0},{0,1},{-1,0},{0,-1}};
        int x=0,y=0;
        int cur=1;
        int d=0;
        while (cur<=n*n){
            ans[y][x]=cur++;
            int nx=x+dir[d][0];
            int ny=y+dir[d][1];
            if (nx<0||nx>=n||ny<0||ny>=n||ans[ny][nx]!=0){
                d=(d+1)%4;
            }
            x+=dir[d][0];
            y+=dir[d][1];
        }
        return ans;
    }
}
