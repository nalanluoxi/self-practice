package likou.二分;

/**
 * @Author 纳兰洛熙
 * @Package：likou.二分
 * @Project：LanQiaoBei
 * @name：搜索二维矩阵
 * @Date：2025/6/26 16:04
 * @Filename：搜索二维矩阵
 */
public class 搜索二维矩阵 {


    public static void main(String[] args) {
        int[][] nums = {
                {1, 3, 5, 7},
                {10, 11, 16, 20},
                {23, 30, 34, 60}};
        System.out.println(searchMatrix(nums, 3));
    }

    public static boolean searchMatrix(int[][] matrix, int target) {
        int n = matrix.length - 1;
        int m = matrix[0].length - 1;
        if (target < matrix[0][0] || target > matrix[n][m]) {
            return false;
        }
        int t = 0, b = n;
        int l = 0, r = m;
        int line = -1;
        while (t <= b) {
            int mid = t + (b - t) / 2;
            if (matrix[mid][l] <= target && matrix[mid][r] >= target) {
                line = mid;
                break;
            } else if (matrix[mid][l] > target) {
                b = mid - 1;
            } else {
                t = mid + 1;
            }
        }
        if (line == -1) {
            return false;
        }
        while (l <= r) {
            int mid = l + (r - l) / 2;
            if (matrix[line][mid] == target) {
                return true;
            } else if (matrix[line][mid] > target) {
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return false;
    }
}
