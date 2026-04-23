package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：最大连续移动次数
 * @Date：2024/12/26 11:28
 * @Filename：最大连续移动次数
 */
public class 最大连续移动次数 {

    public static void main(String[] args) {
        System.out.println(solution(2, 2, new int[][]{{1, 2}, {4, 3}}) == 3);
        System.out.println(solution(3, 3, new int[][]{{10, 1, 6}, {5, 9, 3}, {7, 2, 4}}) == 8);
        System.out.println(solution(4, 4, new int[][]{{8, 3, 2, 1}, {4, 7, 6, 5}, {12, 11, 10, 9}, {16, 15, 14, 13}}) == 11);
    }





    public static int solution(int m, int n, int[][] a) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
        int [][][]db=new int[m][n][2];

        //初始化第一行
        for (int i = 1; i < m; i++) {
            if (a[i][0]==a[i-1][0]){
                continue;
            }else if (a[i][0]>a[i-1][0]){
                //0是下降 1上升
                db[i][0][0]+=db[i-1][0][1];
            } else if (a[i][0]<a[i-1][0]) {
                db[i][0][1]+=db[i-1][0][0];
            }
        }

        //初始化第一列
        for (int i = 1; i < n; i++) {
            if (a[0][i]==a[0][i-1]){
                continue;
            }else if (a[0][i]>a[0][i-1]){
                db[0][i][0]+=db[0][i-1][1];
            } else if (a[0][i]<a[0][i-1]) {
                db[0][i][1]+=db[0][i-1][0];
            }
        }

        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (a[i][i]==a[i-1][j]){
                    continue;
                }else {

                }
            }
        }

        return 0;
    }

}
