package 稀土掘金;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：英雄决斗最大胜利次数
 * @Date：2025/2/9 9:46
 * @Filename：英雄决斗最大胜利次数
 */
public class 英雄决斗最大胜利次数 {


    public static void main(String[] args) {
        //  You can add more test cases here
        int[] heroes1 = {10, 1, 1, 1, 5, 5, 3};
        int[] heroes2 = {1, 1, 1, 1, 1};
        int[] heroes3 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        System.out.println(solution(7, heroes1) == 4);
        System.out.println(solution(5, heroes2) == 0);
        System.out.println(solution(10, heroes3) == 9);
    }

    static int[] hu;

    public static int solution(int n, int[] hf) {
        // Please write your code here
        Arrays.sort(hf);
        hu = new int[n];
        for (int i = 0; i < n; i++) {
            hu[i] = i + 1;
        }
        int win = 0;
        int fi = 0, ui = 0;
        while (fi < n && ui < n) {
          //  System.out.println("hf["+fi+"]="+hf[fi]+" hu["+ui+"]="+hu[ui]);
            if (hf[fi] > hu[ui]) {
                win++;
                ui++;
            //    System.out.println("win: "+win);
            }
            fi++;
        }
        return win;
    }
}
