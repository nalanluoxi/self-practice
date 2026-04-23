package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：整数反转
 * @Date：2025/7/6 12:13
 * @Filename：整数反转
 */
public class 整数反转 {
    public static void main(String[] args) {
        System.out.println(reverse(-120));
        System.out.println(reverse(1534236469));
    }

    public static int reverse(int x) {
        char pre='+';
        if (x<0){
            pre='-';
            x=-x;
        }
        StringBuilder sb  =new StringBuilder(String.valueOf(x));
        String string = sb.reverse().toString();
        Integer i = null;
        try {
            i = Integer.valueOf(string);
        } catch (NumberFormatException e) {
            return 0;
        }
        if (pre=='-'){
            return -i;
        }
        return i;
    }


}
