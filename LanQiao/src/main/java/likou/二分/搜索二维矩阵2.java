package likou.二分;

/**
 * @Author 纳兰洛熙
 * @Package：likou.二分
 * @Project：LanQiaoBei
 * @name：搜索二维矩阵2
 * @Date：2025/6/25 20:28
 * @Filename：搜索二维矩阵2
 */
public class 搜索二维矩阵2 {

    public static void main(String[] args) {
        int [][] nums=new int[][]{{1,4,7,11,15},{2,5,8,12,19},{3,6,9,16,22},{10,13,14,17,24},{18,21,23,26,30}};
        int target = 20;
        System.out.println(searchMatrix(nums, target));
    }

    public static boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;
        int row = 0;
        int col = n - 1;
        while (row<m&&col>=0){
            if (matrix[row][col]==target){
                return true;
            }else if(matrix[row][col]<target){
                row++;
            } else if (matrix[row][col]>target) {
                col--;
            }
        }
        return false;
    }
   /* public static boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;
        int l=0;
        int r=n-1;
        for (int i = 0; i < m; i++) {
            l=0;
            r=n-1;
            while (l<=r){
                int mid=l+(r-l)/2;
                if (matrix[i][mid]==target){
                    return true;
                } else if (matrix[i][mid] < target) {
                    l=mid+1;
                }else {
                    r=mid-1;
                }
            }
            if (l<n&&matrix[i][l]==target){
                return true;
            }
        }
        return false;
    }
*/
}
