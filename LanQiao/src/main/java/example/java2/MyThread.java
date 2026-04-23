package example.java2;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2
 * @Project：LanQiaoBei
 * @name：MyThread
 * @Date：2025/3/20 11:29
 * @Filename：MyThread
 */
public class MyThread extends Thread{

    public void run(int i){
        IFo fo = new FO();
        fo.start(i);
    }
}
