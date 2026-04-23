package example.java2;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2
 * @Project：LanQiaoBei
 * @name：IFo
 * @Date：2025/3/20 11:25
 * @Filename：IFo
 */
public abstract class IFo {

    void start(int i) {
        if (i==1){
            first();
        }else if (i==2){
            second();
        } else if (i==3){
            third();
        }
    }

    abstract void first();
    abstract void second();
    abstract void third();

}
