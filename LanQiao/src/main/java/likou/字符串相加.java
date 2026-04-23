package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：字符串相加
 * @Date：2025/3/31 16:18
 * @Filename：字符串相加
 */
public class 字符串相加 {
    public static void main(String[] args) {
        //String string = addStrings("11", "123");
        String string = addStrings("11111", "1");
        System.out.println(string);
    }

    public static String addStrings(String num1, String num2) {
        int temp = 0;
        int carry = 0;
        StringBuilder res = new StringBuilder("");
        int i = num1.length() - 1, j = num2.length() - 1;
        while(i >= 0 || j >= 0){
            int a = i >= 0 ? num1.charAt(i) - '0': 0;
            int b = j >= 0 ? num2.charAt(j) - '0': 0;
            temp = a + b + carry;
            carry = temp / 10;
            res.append(String.valueOf(temp % 10));
            i--;
            j--;
        }
        if(carry == 1) res.append(carry);
        return res.reverse().toString();
    }
  /*  public static String addStrings(String num1, String num2) {
        String ans = "";
        int maxLength = Math.max(num1.length(), num2.length());
        num1 = String.format("%" + maxLength + "s", num1).replace(' ', '0');
        num2 = String.format("%" + maxLength + "s", num2).replace(' ', '0');
        int len1 = num1.length() - 1;
        int more = 0;
        int len2 = len1;
        while (true) {
            if (len1 < 0 && len2 < 0) {
                break;
            }
            Integer n1 = num1.charAt(len1--) - '0';
            Integer n2 = num2.charAt(len2--) - '0';
            int i = n1 + n2 + more;
            more = i / 10;
            i = i % 10;
            ans = i + ans;
        }
        if (more!=0){
            ans=more+ans;
        }
        return ans;
    }*/
}
