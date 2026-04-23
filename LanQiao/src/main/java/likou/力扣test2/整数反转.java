package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：整数反转
 * @Date：2025/7/15 11:51
 * @Filename：整数反转
 */
public class 整数反转 {
    public static void main(String[] args) {
        //System.out.println(reverse(123));
        System.out.println(reverse(-2147483648));
    }

    public static int reverse(int x) {
       return help(x);
    }

    public static int help(long x){
        if (x<0){
            x=x*-1;
            return -1* help(x);
        }
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(x));
        String s = stringBuilder.reverse().toString();
        Long l = Long.valueOf(s);
        if (l<Integer.MIN_VALUE||l>Integer.MAX_VALUE){
            return 0;
        }
        return Integer.valueOf(s);
    }
}
