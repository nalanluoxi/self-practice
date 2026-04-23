package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：数字1个数
 * @Date：2025/2/7 10:46
 * @Filename：数字1个数
 */
public class 数字1个数 {

    public static void main(String[] args) {

    }
    public static int countDigitOne(int n) {
        String str = String.valueOf(n);
        int count = 0;
        while (!str.equals("")){
            char c = str.charAt(0);
            if (c=='1'){
                count+=1;
            }
        }
        return 0;
    }
}
