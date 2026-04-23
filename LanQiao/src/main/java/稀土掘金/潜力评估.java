package 稀土掘金;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：潜力评估
 * @Date：2025/1/2 10:32
 * @Filename：潜力评估
 */
public class 潜力评估 {




    public static void main(String[] args) {
        System.out.println(solution(5, new int[]{1, 2, 3, 1, 2}) == 3);
        System.out.println(solution(4, new int[]{100000, 100000, 100000, 100000}) == 0);
        System.out.println(solution(6, new int[]{1, 1, 1, 2, 2, 2}) == 3);
    }


    public static int solution(int n, int[] u) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
        int count=0;
        Arrays.sort(u);
        for (int i = n-1; i >=0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                if (u[i] == u[j]){
                    continue;
                }
                if (u[i]>u[j]) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }
}
