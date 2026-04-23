package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：寻找目标值
 * @Date：2025/7/13 11:07
 * @Filename：寻找目标值
 */
public class 寻找目标值 {
    public static void main(String[] args) {
        int[][]nums={
                {2,3,6,8}
                ,{4,5,8,9},
                {5,9,10,12}
        };
        System.out.println(findTargetIn2DPlants(nums,8));
    }

    public static boolean findTargetIn2DPlants(int[][] plants, int target) {
       int i=plants.length-1;
       int j=0;
       while (i>=0 && j<plants[0].length){
           if (plants[i][j]==target){
               return true;
           }if (plants[i][j]>target){
               i--;
           }else {
               j++;
           }
       }
       return false;
    }
}
