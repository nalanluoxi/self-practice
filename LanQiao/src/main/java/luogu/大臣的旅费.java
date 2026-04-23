package luogu;

import java.util.ArrayList;
import java.util.Scanner;

public class 大臣的旅费 {
    public static void main(String[] args) {
        lvfei();
    }
    public static void lvfei() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[][][] allCity = new int[n + 1][n + 1][1];
        for (int i = 1; i < n; i++) {
            int cityname = scanner.nextInt();
            int nextcityname = scanner.nextInt();
            int len = scanner.nextInt();
            allCity[cityname][nextcityname][0] = len;
            allCity[nextcityname][cityname][0] = len;
        }
        ArrayList<Integer> visted = new ArrayList<>();
        int cityn = 1;
        visted.add(cityn);
        while (true) {
            int[] maxlen1 = maxlen(cityn, n, allCity, visted);
            if (maxlen1[0] == -1) {
                break;
            }
            cityn = maxlen1[0];
        }
        visted.clear();
        visted.add(cityn);
        int maxlen = 0;
        while (true) {
            int[] maxlen1 = maxlen(cityn, n, allCity, visted);
            if (maxlen1[0] == -1) {
                break;
            }
            cityn = maxlen1[0];
            int len = maxlen1[1];
            maxlen += len;
        }
        int allmoney = allmoney(maxlen);
        System.out.println(allmoney);
    }
    public static int[] maxlen(int i, int n, int[][][] allCity, ArrayList<Integer> visited) {
        int temmaxlen = 0;
        int temmaxcit = -1;
        for (int j = n; j >= 1; j--) {
            if (j == i||visited.contains(j)) {
                continue;
            }
            if (allCity[i][j][0] > temmaxlen) {
                temmaxlen = allCity[i][j][0];
                temmaxcit = j;
            }
        }
        int[] res = {temmaxcit, temmaxlen};
        visited.add(temmaxcit);
        return res;
    }

    public static int allmoney(int allLen) {
        int money = 10 * allLen + allLen * (1 + allLen) / 2;
        return money;
    }


}
