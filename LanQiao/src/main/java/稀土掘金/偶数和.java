package 稀土掘金;

import java.util.ArrayList;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：偶数和
 * @Date：2024/12/20 16:14
 * @Filename：偶数和
 */
public class 偶数和 {


    public static void main(String[] args) {
        // You can add more test cases here
        System.out.println(solution(new int[]{123, 456, 789}) == 14);
        System.out.println(solution(new int[]{123456789}) == 4);
        System.out.println(solution(new int[]{14329, 7568}) == 10);
    }


    /**
     * 1+1
     * 2+2
     * @param numbers
     * @return
     */

    public static int[][]nums;
    public static int solution(int[] numbers) {
        // Please write your code here
        nums=new int[numbers.length][2];

        for (int i = 0; i < numbers.length; i++) {
            int tempnum = numbers[i];
           int even=0;
           int odd=0;
            while (tempnum!=0){
                int tem = tempnum % 10;
                if (tem%2==0){
                    even++;
                }else {
                    odd++;
                }
            }
            nums[i][0]=even;
            nums[i][1]=odd;
        }

        int count=0;
        return -1;
    }

    public static int getCount(int nowindex,int nowSum,int oddOrEven){
        if (nowindex== nums.length){
            return nowSum;
        }
        if (oddOrEven==0){
            if (nums[nowindex+1][1]==0){
                getCount(nowindex+1,nowSum , oddOrEven);
            }else {

            }
        }else {

        }


        return 0;
    }

}
