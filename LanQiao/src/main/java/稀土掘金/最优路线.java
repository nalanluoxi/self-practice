package 稀土掘金;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：最优路线
 * @Date：2025/1/24 10:21
 * @Filename：最优路线
 */
public class 最优路线 {


   /* public static void main(String[] args) {
        List<List<Integer>> gasStations1 = new ArrayList<>();
        gasStations1.add(List.of(100, 1));
        gasStations1.add(List.of(200, 30));
        gasStations1.add(List.of(400, 40));
        gasStations1.add(List.of(300, 20));

        List<List<Integer>> gasStations2 = new ArrayList<>();
        gasStations2.add(List.of(100, 999));
        gasStations2.add(List.of(150, 888));
        gasStations2.add(List.of(200, 777));
        gasStations2.add(List.of(300, 999));
        gasStations2.add(List.of(400, 1009));
        gasStations2.add(List.of(450, 1019));
        gasStations2.add(List.of(500, 1399));

        List<List<Integer>> gasStations3 = new ArrayList<>();
        gasStations3.add(List.of(101));
        gasStations3.add(List.of(100, 100));
        gasStations3.add(List.of(102, 1));

        List<List<Integer>> gasStations4 = new ArrayList<>();
        gasStations4.add(List.of(34, 1));
        gasStations4.add(List.of(105, 9));
        gasStations4.add(List.of(9, 10));
        gasStations4.add(List.of(134, 66));
        gasStations4.add(List.of(215, 90));
        gasStations4.add(List.of(999, 1999));
        gasStations4.add(List.of(49, 0));
        gasStations4.add(List.of(10, 1999));
        gasStations4.add(List.of(200, 2));
        gasStations4.add(List.of(300, 500));
        gasStations4.add(List.of(12, 34));
        gasStations4.add(List.of(1, 23));
        gasStations4.add(List.of(46, 20));
        gasStations4.add(List.of(80, 12));
        gasStations4.add(List.of(1, 1999));
        gasStations4.add(List.of(90, 33));
        gasStations4.add(List.of(101, 23));
        gasStations4.add(List.of(34, 88));
        gasStations4.add(List.of(103, 0));
        gasStations4.add(List.of(1, 1));

        System.out.println(solution(500, 4, gasStations1) == 4300);
       //System.out.println(solution(500, 7, gasStations2) == 410700);
       //System.out.println(solution(500, 3, gasStations3) == -1);
       //System.out.println(solution(100, 20, gasStations4) == 0);
       //System.out.println(solution(100, 0, new ArrayList<>()) == -1);
    }
*/
    public static int solution(int distance, int n, List<List<Integer>> gas_stations) {
        // Please write your code here
        Collections.sort(gas_stations, (a, b) -> a.get(0) - b.get(0));

        int nowstage=0;
        int nowoil=200;
        int minoil=Integer.MAX_VALUE;
        int minindex=0;
        int allMoney=0;

        if (gas_stations.get(0).get(0)>200){
            return -1;
        }else {
            nowoil=200-gas_stations.get(0).get(0);
            nowstage=gas_stations.get(0).get(0);
        }
        int allminoil=gas_stations.get(0).get(1);
        System.out.println("现在距离期起点站："+nowstage+" 现在油量："+nowoil);
        while (minindex<gas_stations.size()-1){
            minindex++;
            minoil=gas_stations.get(minindex).get(1);
            for (int i = minindex; i <gas_stations.size(); i++) {
                if (gas_stations.get(i).get(0)>nowstage+400){
                    System.out.println("现在距离起点站："+nowstage+" 现在油量："+nowoil+"找到400米内最便宜的加油站"+minindex+"买"+minoil+"元");
                    break;
                }
                if (gas_stations.get(i).get(1)<minoil){
                    minoil=gas_stations.get(i).get(1);
                    minindex=i;
                }
            }
            System.out.println("在"+minindex+"加油站买"+(gas_stations.get(minindex).get(0)-nowstage)+"的油");
            allMoney+=minoil*(gas_stations.get(minindex).get(0)-nowstage);
            nowstage=gas_stations.get(minindex).get(0);
        }

        System.out.println(allMoney);
        return allMoney;
    }

}
