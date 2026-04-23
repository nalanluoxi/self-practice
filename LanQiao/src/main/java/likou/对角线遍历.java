package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：对角线遍历
 * @Date：2025/6/11 10:07
 * @Filename：对角线遍历
 */
public class 对角线遍历 {

    public static void main(String[] args) {
        int[][]nums={{01,02,03},
                    {10,11,12},
                    {20,21,22},
                    {30,31,32}};
        // 00  01  02  03  04
        // 10  11  12  13  14
        // 20  21  22  23  24
        // 30  31  32  33  34
        int[] diagonalOrder = findDiagonalOrder2(nums);
        int[] diagonalOrder1 = findDiagonalOrder(nums);
        for (int i = 0; i < diagonalOrder.length; i++) {
            System.out.print(diagonalOrder[i]+" ");
        }
        System.out.println();
        System.out.println("================================");
        for (int i = 0; i < diagonalOrder1.length; i++) {
            System.out.print(diagonalOrder1[i]+" ");
        }
    }

    public static int[] findDiagonalOrder(int[][] mat) {
        int m=mat.length;
        int n=mat[0].length;
        int [] ans=new int[n*m];
        int index=0;
        for (int i = 0; i < m+n-1; i++) {
            if (i%2==1){
                int x=i<n? 0: i-n+1;
                int y=i<n? i : n-1;
                while (x<m && y>=0){
                    System.out.println("x="+x+"y="+y+"index="+index);
                    ans[index++]=mat[x++][y--];
                }
            }else{
                int x= i<m? i : m-1;
                int y= i<m? 0:i-m+1;
                while (x>=0 && y<n){
                    System.out.println("x="+x+" y="+y+" index="+index);
                    ans[index++]=mat[x--][y++];
                }
            }
        }
        return ans;
    }


    public static int[] findDiagonalOrder2(int[][] mat) {
        int m = mat.length;
        int n = mat[0].length;
        int[] res = new int[m * n];
        int pos = 0;
        for (int i = 0; i < m + n - 1; i++) {
            if (i % 2 == 1) {
                int x = i < n ? 0 : i - n + 1;
                int y = i < n ? i : n - 1;
                while (x < m && y >= 0) {
                    System.out.println("x="+x+" y="+y+" pos="+pos);

                    res[pos] = mat[x][y];
                    pos++;
                    x++;
                    y--;
                }
            } else {
                int x = i < m ? i : m - 1;
                int y = i < m ? 0 : i - m + 1;
                while (x >= 0 && y < n) {
                    System.out.println("x="+x+" y="+y+" pos="+pos);

                    res[pos] = mat[x][y];
                    pos++;
                    x--;
                    y++;
                }
            }
        }
        return res;
    }
}
