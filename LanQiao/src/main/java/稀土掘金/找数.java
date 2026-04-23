package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：找数
 * @Date：2024/12/23 16:46
 * @Filename：找数
 */
public class 找数 {


    public static void main(String[] args) {
        // Add your test cases here

        System.out.println(solution(new int[]{1,3,8,2,3,1,3,3,3}));
    }

    public static int solution(int[] array) {
        // Edit your code here
        int[] nums=new int[100];
        for (int temn : array) {
            nums[temn]++;
        }
        for (int i = 0; i < nums.length; i++) {
            if (nums[i]>(array.length/2)){
                return i;
            }
        }
        return 0;
    }


}
