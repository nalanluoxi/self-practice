package 设计模式.结构模式.代理模式;

/**
 * @Author 纳兰洛熙
 * @Package：设计模式.结构模式.代理模式
 * @Project：LanQiaoBei
 * @name：Test
 * @Date：2025/5/19 8:37
 * @Filename：Test
 */
public class Test {
    public static void main(String[] args) {
        JDKProxFactory px = new JDKProxFactory();
        SellTickets proxyObject = px.getProxyObject();
        proxyObject.sell();
        proxyObject.show();
    }
}
