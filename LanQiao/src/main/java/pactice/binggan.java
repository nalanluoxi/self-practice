package pactice;

import java.util.Arrays;

public class binggan {
    public static void main(String[] args) {
       int []g={1,2};
       int []s={1,3,2};
       // System.out.println(findContentChildren(g,s));
        //System.out.println(ordernumber(s));
        findContentChildren(g,s);
    }

    public static int findContentChildren(int[] g, int[] s) {
        Arrays.sort(g);
        Arrays.sort(s);
        int count = 0;
        int start = s.length - 1;
        // 遍历胃口
        for (int index = g.length - 1; index >= 0; index--) {
            if(start >= 0 && g[index] <= s[start]) {
                start--;
                count++;
            }
        }
        System.out.println(count);
        return count;
    }

    /*

    public static int findContentChildren(int[] g, int[] s) {
        int num=0;
        g=ordernumber(g);
        s=ordernumber(s);
        for (int i = 0; i < g.length; i++) {
            for (int j = 0; j < s.length; j++) {
                if (s[j]>=g[i]){
                    num++;
                    s[j]=0;
                    break;
                }
            }
        }
        return num;
    }

    public static int[] ordernumber(int []array){
        for (int i = 1; i < array.length; i++) {
            for (int j = 0; j < array.length - i; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
        return array;
    }
*/
}
