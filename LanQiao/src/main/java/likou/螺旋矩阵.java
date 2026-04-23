package likou;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：螺旋矩阵
 * @Date：2025/1/24 17:45
 * @Filename：螺旋矩阵
 */
public class 螺旋矩阵 {
    public static void main(String[] args) {
        int [][] matrix={{1,2,3},{4,5,6},{7,8,9}};
        List<Integer> integers = spiralOrder(matrix);
        for (Integer integer : integers) {
            System.out.print(integer+" ");
        }
    }

    static int[][]nums;
    static List<Integer> res;
    public static List<Integer> spiralOrder(int[][] matrix) {
        res=new ArrayList<>();
        nums=matrix;

        int t=0;
        int l=0;
        int r=matrix[0].length-1;
        int b=matrix.length-1;
        int line =r;
        int h=0;
        while (res.size()!=matrix.length*matrix[0].length){
            if (res.size()!=matrix.length*matrix[0].length){
                printLTOR(l,r,h);
                h=b;
                t++;
            }
            if (res.size()!=matrix.length*matrix[0].length){
                printTOLB(t,b,line);
                line=l;
                r--;
            }
            if (res.size()!=matrix.length*matrix[0].length){
                printRTOL(l,r,h);
                h=t;
                b--;
            }
            if (res.size()!=matrix.length*matrix[0].length){
                printBOTL(t,b,line);
                line=r;
                l++;
            }
        }
        return res;
    }

    public static void printLTOR(int l,int r,int h){
        for (int i = l; i <= r ; i++) {
            //System.out.println(nums[h][i]);
            res.add(nums[h][i]);
        }
    }

    public static void printRTOL(int l,int r,int h){
        for (int i = r; i >= l ; i--) {
          //  System.out.println(nums[h][i]);
            res.add(nums[h][i]);
        }
    }

    public static void printTOLB(int t,int b,int line){
        for (int i = t; i <= b ; i++) {
         //   System.out.println(nums[i][line]);
            res.add(nums[i][line]);
        }
    }

    public static void printBOTL(int t,int b,int line){
        for (int i = b; i >= t ; i--) {
           // System.out.println(nums[i][line]);
            res.add(nums[i][line]);
        }
    }
}
