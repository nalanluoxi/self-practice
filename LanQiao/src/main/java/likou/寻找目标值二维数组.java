package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：寻找目标值二维数组
 * @Date：2025/7/11 15:50
 * @Filename：寻找目标值二维数组
 */
public class 寻找目标值二维数组 {
    public static void main(String[] args) {
        int [][] nums=new int[][]{
                {1,4,7,11,15},
                {2,5,8,12,19},
                {3,6,9,16,22},
                {10,13,14,17,24},
                {18,21,23,26,30}
        };
       // System.out.println(findTargetIn2DPlants(nums,20));
        System.out.println(findTargetIn2DPlants(new int[][]{},20));
    }

    public static boolean findTargetIn2DPlants(int[][] nums, int target) {
        int i=nums.length-1;
        int j=0;
        while (i>=0&&j<nums[0].length){
            if (nums[i][j]==target){
                return true;
            } else if (nums[i][j] > target) {
                i--;
            } else if (nums[i][j] < target) {
                j++;
            }
        }
        return false;
    }
}
