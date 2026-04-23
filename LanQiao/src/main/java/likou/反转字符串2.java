package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：反转字符串2
 * @Date：2025/2/14 9:52
 * @Filename：反转字符串2
 */
public class 反转字符串2 {
    public static void main(String[] args) {
        System.out.println(reverseStr("abcdefg", 3));
    }

    public static String reverseStr(String s, int k) {
        if (s.length() < k) {
            return new StringBuilder(s).reverse().toString();
        }
        int ptr = 0;
        StringBuilder str = new StringBuilder();
        while (ptr < s.length()) {
            if (s.length() - ptr < k) {
                str.append(new StringBuilder(s.substring(ptr)).reverse());
                break;
            } else if (s.length() - ptr < 2 * k && s.length() - ptr >= k) {
                str.append(new StringBuilder(s.substring(ptr,ptr+k)).reverse());
                str.append(s.substring(ptr+k));
                break;
            }
            str.append(new StringBuilder(s.substring(ptr,ptr+k)).reverse());
            str.append(s.substring(ptr+k,ptr+2*k));
            ptr+=2*k;
        }
        return str.toString();
    }
}
