package 稀土掘金;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：特定数组
 * @Date：2024/12/24 20:00
 * @Filename：特定数组
 */
public class 特定数组 {

    public static void main(String[] args) {
        System.out.println(Arrays.equals(solution(3), new int[]{3, 2, 1, 3, 2, 3}));
        System.out.println(Arrays.equals(solution(4), new int[]{4, 3, 2, 1, 4, 3, 2, 4, 3, 4}));
        System.out.println(Arrays.equals(solution(5), new int[]{5, 4, 3, 2, 1, 5, 4, 3, 2, 5, 4, 3, 5, 4, 5}));
    }


    public static int[] solution(int n) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
        int length=(n*(n+1))/2;
        int[] res=new int[length];
        int temp=n;
        int point=0;
        for (int i = 0; i < res.length; i++) {
            if (temp==point){
                temp=n;
                point++;
            }
            res[i]=temp;
            temp--;
        }

        return res;
    }

}
