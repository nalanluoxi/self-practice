package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：反转字符串
 * @Date：2025/2/14 9:45
 * @Filename：反转字符串
 */
public class 反转字符串 {
    public static void main(String[] args) {
        char []s={'h','e','l','l','o'};
        reverseString(s);
        for (char c : s) {
            System.out.println(c);
        }
    }

    public static void reverseString(char[] s) {
        int left=0;
        int right=s.length-1;
        while (left<=right){
            char temp=s[left];
            s[left]=s[right];
            s[right]=temp;
            left++;
            right--;
        }
    }



}
