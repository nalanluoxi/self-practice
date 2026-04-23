package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：字符串相加
 * @Date：2025/6/4 19:53
 * @Filename：字符串相加
 */
public class 字符串相加 {

    public static void main(String[] args) {
        //System.out.println(addStrings("123", "456"));
        //System.out.println(addStrings("123", "11"));
        //System.out.println(addStrings("1", "9"));
        System.out.println(addStrings("99", "9"));
    }

    public String addStrings1(String num1, String num2) {
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

    public static String addStrings(String num1, String num2) {
        int temp = 0;
        StringBuilder sb = new StringBuilder("");
        int l1 = num1.length() - 1;
        int l2 = num2.length() - 1;
        while (l1 >= 0 || l2 >= 0) {
            int n1 = l1 >= 0 ? num1.charAt(l1--) - '0' : 0;
            int n2 = l2>=0?num2.charAt(l2--) - '0':0;
            int sum = n1 + n2 + temp;
            temp = sum / 10;
            sb.append(sum % 10);
        }
        if (temp != 0) {
            sb.append(temp);
        }
        return sb.reverse().toString();
    }


}
