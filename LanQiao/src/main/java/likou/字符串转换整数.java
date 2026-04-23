package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：字符串转换整数
 * @Date：2025/4/25 17:00
 * @Filename：字符串转换整数
 */
public class 字符串转换整数 {
    public static void main(String[] args) {
       // System.out.println(myAtoi("-91283472332"));
        System.out.println(myAtoi("+-12"));
    }

  /*  public static int myAtoi(String s) {
        if (s == null || s.length() == 0){
            return 0;
        }
*//*        if (s.equals("-91283472332")){
            return -2147483648;
        } else if (s.equals("-91283472332")) {
            return 2147483647;
        }*//*

        String ans = "";
        int b = 1;
        while (s.length()>0&&s.charAt(0) == ' ') {
            s = s.substring(1);
        }
        if (s == null || s.length() == 0){
            return 0;
        }
        if (s.charAt(0) == '-') {
            b = -1;
            s = s.substring(1);
        } else if (s.charAt(0) == '+') {
            b = 1;
            s = s.substring(1);
        } else if (s.charAt(0) < '0' || s.charAt(0) > '9') {
            return 0;
        }
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                ans += c;
            } else {
                break;
            }
        }

        if (ans==""){
            return 0;
        }
        long l = Long.parseLong(ans);

        if (l > Integer.MAX_VALUE) {
            l= Integer.MAX_VALUE+1;
            if (b == 1){
                return (int) l-1;
            } else if (b==-1) {
                return (int) -l;
            }
        }
        return b == 1 ? (int) l-1 : (int) -l;
    }*/


    public static int myAtoi(String str) {
        int len = str.length();
        // str.charAt(i) 方法回去检查下标的合法性，一般先转换成字符数组
        char[] charArray = str.toCharArray();

        // 1、去除前导空格
        int index = 0;
        while (index < len && charArray[index] == ' ') {
            index++;
        }

        // 2、如果已经遍历完成（针对极端用例 "      "）
        if (index == len) {
            return 0;
        }

        // 3、如果出现符号字符，仅第 1 个有效，并记录正负
        int sign = 1;
        char firstChar = charArray[index];
        if (firstChar == '+') {
            index++;
        } else if (firstChar == '-') {
            index++;
            sign = -1;
        }

        // 4、将后续出现的数字字符进行转换
        // 不能使用 long 类型，这是题目说的
        int res = 0;
        while (index < len) {
            char currChar = charArray[index];
            // 4.1 先判断不合法的情况
            if (currChar > '9' || currChar < '0') {
                break;
            }

            // 题目中说：环境只能存储 32 位大小的有符号整数，因此，需要提前判：断乘以 10 以后是否越界
            if (res > Integer.MAX_VALUE / 10 || (res == Integer.MAX_VALUE / 10 && (currChar - '0') > Integer.MAX_VALUE % 10)) {
                return Integer.MAX_VALUE;
            }
            if (res < Integer.MIN_VALUE / 10 || (res == Integer.MIN_VALUE / 10 && (currChar - '0') > -(Integer.MIN_VALUE % 10))) {
                return Integer.MIN_VALUE;
            }

            // 4.2 合法的情况下，才考虑转换，每一步都把符号位乘进去
            res = res * 10 + sign * (currChar - '0');
            index++;
        }
        return res;
    }

}
