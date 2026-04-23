package 设计模式.结构模式.代理模式;

/**
 * @Author 纳兰洛熙
 * @Package：设计模式.结构模式.代理模式
 * @Project：LanQiaoBei
 * @name：TrainStation
 * @Date：2025/5/19 8:34
 * @Filename：TrainStation
 */
public class TrainStation implements SellTickets{
    @Override
    public void sell() {
        System.out.println("火车站TrainStation卖票");
    }

    @Override
    public void show() {
        System.out.println("火车站TrainStation展示");
    }


}
