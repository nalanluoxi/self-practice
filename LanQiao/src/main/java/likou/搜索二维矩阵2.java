package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：搜索二维矩阵2
 * @Date：2025/5/10 20:15
 * @Filename：搜索二维矩阵2
 */
public class 搜索二维矩阵2 {
    public static void main(String[] args) {
        /*int[][] nums = {
                {1, 4, 7, 11, 15},
                {2, 5, 8, 12, 19},
                {3, 6, 9, 16, 22},
                {10, 13, 14, 17, 24},
                {18, 21, 23, 26, 30}
        };*/
        int[][] nums = {
                {1,2,3,7,8},
                {5,10,14,16,19},
                {8,10,18,19,23},
                {9,12,22,24,29}
        };
        System.out.println(searchMatrix(nums, 14));
    }

    public static boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length-1;
        int n = matrix[0].length-1;
        int x=0;
        int y=n;
        while (x<=m && y>=0){
            if(matrix[x][y]==target){
                return true;
            }else if(matrix[x][y]>target){
                y--;
            }else if(matrix[x][y]<target){
                x++;
            }
        }
        return false;
    }


 /*   public static boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length-1;
        int n = matrix[0].length-1;
        int l=0;
        int r=m;
        int mid=0;
        while (l<=r){
            mid=l+(r-l)/2;
            if(matrix[mid][0]==target){
                return true;
            }else if(matrix[mid][0]<target){
                l=mid+1;
            } else if (matrix[mid][0] > target) {
                r=mid-1;
            }
        }
        if(matrix[mid][0]>target){
            mid--;
        }
        l=0;
        r=n;
        int mid1=0;
        while (mid>=0){
            while (l<=r){
                mid1=l+(r-l)/2;
                if(matrix[mid][mid1]==target){
                    return true;
                }else if(matrix[mid][mid1]<target){
                    l=mid1+1;
                }else if(matrix[mid][mid1]>target){
                    r=mid1-1;
                }
            }
            if (matrix[mid][mid1] == target) {
                return true;
            }else {
                mid--;
                l=0;
                r=n;
            }
        }
        return false;
    }

*/

}
