package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：字符串相乘
 * @Date：2025/4/26 21:14
 * @Filename：字符串相乘
 */
public class 字符串相乘 {
    public static void main(String[] args) {
        System.out.println(multiply("123", "456"));
        //System.out.println(cheng('2', "123", 1));
        // System.out.println(add("111123", "456"));
        //System.out.println(multiply("123", "456"));
    }





    public static String multiply(String num1, String num2) {
        if (num1.equals("0") || num2.equals("0")) {
            return "0";
        }
        if (num1.length()<num2.length()){
            return help(num1,num2);
        }else {
            return help(num2,num1);
        }
    }

    public static String help(String min, String max) {
        int lmin = min.length();
        String ans = "";
        int jie=0;
        for (int i = lmin-1; i >=0; i--) {
            String cheng = cheng(min.charAt(i), max, jie++);
            ans = add(ans,cheng);
        }
        return ans;
    }


    public static String cheng(char min, String max, int jie) {
        String ans="";
        int n=min-'0';
        if (n==0){
            return "0";
        }
        int temp = 0;
        for (int i = max.length()-1; i >=0; i--) {
            int m = max.charAt(i) - '0';
            int sum = m * n + temp;
            temp = sum / 10;
            int now = sum % 10;
            ans = String.valueOf(now) + ans;
        }
        if (temp!= 0) {
            ans = String.valueOf(temp) + ans;
        }
        for (int i = 0; i < jie; i++) {
            ans = ans + "0";
        }
        return ans;
    }



    public static String add(String num1, String num2) {
        int len1 = num1.length()-1;
        int len2 = num2.length()-1;
        String ans = "";
        int temp = 0;
        while (len1 >= 0 || len2 >= 0) {
            int n1 = len1 >= 0 ? num1.charAt(len1--) - '0' : 0;
            int n2 = len2 >= 0 ? num2.charAt(len2--) - '0' : 0;
            int sum = n1 + n2 + temp;
            temp = sum / 10;
            int now = sum % 10;
            ans = String.valueOf(now) + ans;
        }
        if (temp != 0) {
            ans = String.valueOf(temp) + ans;
        }
        return ans;
    }

}
