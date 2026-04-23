package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：旋转矩阵
 * @Date：2025/1/24 20:04
 * @Filename：旋转矩阵
 */
public class 旋转矩阵 {
    public static void main(String[] args) {
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        rotate(matrix);
        for (int[] ints : matrix) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
    }


    public static void rotate(int[][] matrix) {
        int len = matrix.length;
        nums=new  int[len][len];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                nums[j][len-1-i]=matrix[i][j];
            }
        }
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                matrix[i][j]=nums[i][j];
            }
        }
    }

    static int[] tempnums;
    static int[][] nums;

/*    public static void rotate(int[][] matrix) {
        int len = matrix.length;
        nums = matrix;
        tempnums = new int[len];
        int start=0;
        int end=len-1;
        boolean isTwo=false;
        while (start!=end){
            moveXTOY(start, end, start,end,isTwo);
            isTwo=true;
            moveYTOX(start, end, end, end);
            moveXTOY(start,end,end,start,isTwo);
            moveYTOX(start,end,start,start);
            start++;
            end--;
        }

    }

    public static void moveYTOX(int start, int end, int x, int y) {
        for (int i = start; i < end; i++) {
            int n = nums[y][i];
            //tempnums[i] = nums[y][i];
            nums[y][i] = tempnums[i];
            tempnums[i]=n;
        }
        //tempnums[end] = nums[y][end];
        nums[y][end]= tempnums[0];
    }
    public static void moveXTOY(int start, int end, int x, int y,boolean isTwo) {
        for (int i = start; i < end; i++) {
            if (!isTwo){
                tempnums[i] = nums[i][y];
                nums[i][y] = nums[x][i];
            }else {
                int n = nums[i][y];
                nums[i][y] = tempnums[i];
                tempnums[i]=n;
            }
        }
        tempnums[end] = nums[end][y];
        nums[end][y]= tempnums[0];
    }*/








}
