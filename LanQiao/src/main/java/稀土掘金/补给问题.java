package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：补给问题
 * @Date：2024/12/19 16:28
 * @Filename：补给问题
 */
public class 补给问题 {
    public static void main(String[] args) {
        /*  System.out.println(solution(5, 2, new int[]{1, 2, 3, 3, 2}));*/
        //System.out.println(solution(6, 3, new int[]{4, 1, 5, 2, 1, 3}) == 9);
        System.out.println(solution(5, 2, new int[]{1, 2, 3, 3, 2}) == 9);

    }


    public static int solution(int n, int k, int[] data) {
        // Edit your code here
        int allMoney = 0;
        int minPrice = data[0];
        int minindex = 0;
        for (;;) {
            int tempmin = Integer.MAX_VALUE;
            int tempminindex = -1;
            for (int i = minindex+1; i <= Math.min(minindex + k ,data.length-1); i++) {
                if (data[i] < tempmin) {
                    tempmin = data[i];
                    tempminindex = i;
                }
            }
            allMoney += minPrice * (tempminindex - minindex);
            minindex = tempminindex;
            minPrice = tempmin;
            if (minindex >=n-k&&minindex<n){
                break;
            }
        }
        allMoney += minPrice * (n - minindex);
        System.out.println(allMoney);
        return allMoney;
    }

    /*public static int solution(int n, int k, int[] prices) {
        int totalCost = 0;
        int currentFood = 0; // 当前携带的食物数量
        int nowstage = 0;//当前节点号
        while (nowstage < n) {
            //如果食物够到达终点，直接结束/
            if (nowstage + currentFood == n ) {
                return totalCost;
            }
            //寻找最低价的车站
            int minPrice = prices[nowstage];
            int minIndex = nowstage;
            for (int i = nowstage+1; i < Math.min(nowstage + k, n); i++) {
                if (prices[i] < minPrice) {
                    minPrice = prices[i];
                    minIndex = i;
                }
            }
            //如果当前站是最便宜的，加满油找第二便宜的
            int buyFoodNumber = 0;
            if (minIndex == nowstage) {
                int min2Price = prices[nowstage + 1];
                int min2Index = nowstage + 1;
                for (int i = nowstage + 1;i < Math.min(nowstage + k, n); i++) {
                    if (prices[i] <= min2Price) {
                        min2Price = prices[i];
                        min2Index = i;
                    }
                }
                buyFoodNumber =k-currentFood;
                minIndex = min2Index;
            }else {
                buyFoodNumber=minIndex-nowstage;
            }
            System.out.println("nostage:"+nowstage);
            //加油
            currentFood += buyFoodNumber;
            System.out.println("加油量:"+buyFoodNumber);
            System.out.println("当前油量:"+currentFood);
            //计算车费
            totalCost += buyFoodNumber * prices[nowstage];
            System.out.println("当前费用:"+totalCost);
            //去往下一个便宜的车站，消费油，
            currentFood -= (minIndex - nowstage);
            nowstage = minIndex;
            System.out.println("下一站："+nowstage);
            System.out.println();
        }
        return totalCost;
    }
*/
}
