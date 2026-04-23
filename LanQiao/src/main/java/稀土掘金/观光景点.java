package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：观光景点
 * @Date：2024/12/24 19:45
 * @Filename：观光景点
 */
public class 观光景点 {


    public static void main(String[] args) {
        System.out.println(solution(new int[]{8, 3, 5, 5, 6}) == 11 ? 1 : 0);
        System.out.println(solution(new int[]{10, 4, 8, 7}) == 16 ? 1 : 0);
        System.out.println(solution(new int[]{1, 2, 3, 4, 5}) == 8 ? 1 : 0);
    }



    public static int solution(int[] values) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
        int [] grade=new int[values.length];
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values.length; j++) {
                if (i==j){
                    continue;
                }
                grade[i]=Math.max(grade[i],(values[i]+values[j]+Math.min(i,j)-Math.max(i,j)));
            }
        }
        int maxnum=0;
        for (int i : grade) {
            maxnum=Math.max(maxnum, i);
        }
        return maxnum; // Placeholder return
    }
}
