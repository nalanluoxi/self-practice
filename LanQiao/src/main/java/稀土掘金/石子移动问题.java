package 稀土掘金;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：石子移动问题
 * @Date：2024/12/28 17:15
 * @Filename：石子移动问题
 */
public class 石子移动问题 {


    public static void main(String[] args) {
        //System.out.println(solution(new int[]{7, 4, 9}) == 2);
        System.out.println(solution(new int[]{6, 5, 4, 3, 10}) == 3);
       // System.out.println(solution(new int[]{1, 2, 3, 4, 5}) == 0);
    }

    public static int solution(int[] stones) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
       // Arrays.sort(stones);
        int count = 0;
        int[] ansNums = getAnsNums(stones);
        for (int ansNum : ansNums) {
            System.out.println(ansNum);
        }


        return count;
    }

    public static int[] getAnsNums(int[] nums){
        int len = nums.length;
        int[]res=new int[len];
        int total=sum(nums);
        int a1=(total-len*(len-1)/2)/len;
        for (int i = 0; i < len; i++) {
            res[i]=a1;
            a1++;
        }
        return res;
    }

    public static int sum(int[] num){
        int res=0;
        for (int i : num) {
            res+=i;
        }
        return res;
    }

}
