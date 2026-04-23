package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：可获得的最大点数
 * @Date：2025/7/12 10:29
 * @Filename：可获得的最大点数
 */
public class 可获得的最大点数 {
    public static void main(String[] args) {
        int[]num={1,2,3,4,5,6,1};
        System.out.println(maxScore(num,3));
    }

    public static int maxScore(int[] cardPoints, int k) {
        int windSize=cardPoints.length-k;
        int winSum=0;
        for (int i = 0; i < windSize; i++) {
            winSum+=cardPoints[i];
        }
        int minSum=winSum;
        for (int i = windSize; i < cardPoints.length; i++) {
            winSum+=cardPoints[i]-cardPoints[i-windSize];
            minSum=Math.min(minSum,winSum);
        }
        int sum=0;
        for (int i = 0; i < cardPoints.length; i++) {
            sum+=cardPoints[i];
        }
        return sum-minSum;
    }
}
