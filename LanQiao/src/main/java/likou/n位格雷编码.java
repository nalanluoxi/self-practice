package likou;

import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：n位格雷编码
 * @Date：2025/2/6 17:53
 * @Filename：n位格雷编码
 */
public class n位格雷编码 {
    public static void main(String[] args) {
       /* List<Integer> list = grayCode(2);
        for (Integer integer : list) {
            System.out.println(integer);
        }*/
       // System.out.println(getTwo(2));
        for (int i = 0; i < 4; i++) {
            System.out.println(getTwo(i));
        }
    }

    public static List<Integer> grayCode(int n) {
        for (int i = 0; i <= n; i++) {
            System.out.println(grayCode(i));
        }
        return null;
    }

    public static String getTwo(int n){
        if (n==0){
            return "0";
        }
        String ans="";
        while (n!=0){
            int i = n % 2;
            ans=i+ans;
            n/=2;
        }

        return ans;
    }

}
