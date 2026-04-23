package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：完美偶数
 * @Date：2025/1/21 18:19
 * @Filename：完美偶数
 */
public class 完美偶数 {


    public static void main(String[] args) {
        System.out.println(solution(5, 3, 8, new int[]{1, 2, 6, 8, 7}) == 2);
        System.out.println(solution(4, 10, 20, new int[]{12, 15, 18, 9}) == 2);
        System.out.println(solution(3, 1, 10, new int[]{2, 4, 6}) == 3);
    }

    public static int solution(int n, int l, int r, int[] a) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
        int count=0;
        for (int num : a) {
            if (num%2!=0){
                continue;
            }
            if (num>=l&&num<=r){
                count++;
            }
        }

        return count;
    }
}
