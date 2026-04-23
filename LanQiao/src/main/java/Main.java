import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int G=11;
        int mod=17;
        int a=5;
        int b=7;
        int A = jiami(G, mod, a);
        int B = jiami(G, mod, b);
        int k1 = jiami(A, mod, b);
        int k2 = jiami(B, mod, a);
        if (k1==k2){
            System.out.println("秘钥相同");
        }
    }

    public static int jiami(int D ,int mod,int s){
        System.out.println("------------------");
        System.out.println("底数："+D+" 指数："+s+" 模："+mod);
        double pow = Math.pow(D, s);
        System.out.println(pow);
        double ans = pow % mod;
        System.out.println(ans);
        System.out.println("------------------");
        return (int) ans;
    }




}
