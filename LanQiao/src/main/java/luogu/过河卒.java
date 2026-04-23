package luogu;

import java.util.Scanner;

public class 过河卒 {
    public static void main(String[] args) {
        guohe();
    }

    public static long[][] arr = new long[25][25];
    public static int[][] nxt ={{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{1,-2},{-1,2},{-1,-2}};
    public static long[][] db = new long[25][25];

    public static void guohe() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int cx = scanner.nextInt();
        int cy = scanner.nextInt();
        arr[cx][cy] = -2;
        for(int i=0;i<8;i++){
            int nx=cx+nxt[i][0];
            int ny=cy+nxt[i][1];
            if(nx>=0&&ny>=0&&nx<=n&&ny<=m)arr[nx][ny]=-2;
        }
        for (int i = 0; i <= n; i++) {
            if (arr[i][0] != -2) {
                db[i][0] = 1;
            }
            else break;
        }
        for (int i = 0; i <= m; i++) {
            if (arr[0][i] != -2) {
                db[0][i] = 1;
            }
            else break;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (arr[i][j] != -2) {
                    db[i][j] = db[i - 1][j] + db[i][j - 1];
                }

            }
        }
        System.out.println(db[n][m]);
    }


}
