package 设计模式.结构模式.代理模式;

import java.lang.reflect.Proxy;

/**
 * @Author 纳兰洛熙
 * @Package：设计模式.结构模式.代理模式
 * @Project：LanQiaoBei
 * @name：JDKProxFactory
 * @Date：2025/5/19 8:33
 * @Filename：JDKProxFactory
 */
public class JDKProxFactory {

    private TrainStation trainStation=new TrainStation();

    public SellTickets getProxyObject(){
        return (SellTickets) Proxy.newProxyInstance(
                trainStation.getClass().getClassLoader(),
                trainStation.getClass().getInterfaces(),
                (proxy, method, args) -> {
                    System.out.println("代理点收取一些服务费用JDK动态代理");
                    Object res = method.invoke(trainStation, args);
                    System.out.println(res);
                    return res;
                });
    }



}
