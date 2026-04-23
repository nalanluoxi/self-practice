package luogu;

public class 三连击 {
    public static void main(String[] args) {
        sanlianji();
    }

    public static void sanlianji(){
        int target1=0;
        int target2=0;
        int target3=0;
        for (int i = 123; i < 999; i++) {
            target1=i;
            target2=target1*2;
            target3=target1*3;
            addHa(target1);
            addHa(target2);
            addHa(target3);
            if (isTrue()){
                System.out.println(target1+" "+target2+" "+target3);
            }
            remove();
        }
    }

    public static int ha[]=new int[10];
    public static void  addHa(int a){
        int i=a%10;
        ha[i]++;
        while ((a=a/10)!=0){
            i=a%10;
            ha[i]++;
        }
    }
    public static void remove(){
        for (int i = 0; i < ha.length; i++) {
            ha[i]=0;
        }
    }

    public static boolean isTrue(){
        for (int i = 1; i < ha.length; i++) {
            if (ha[i]!=1){
                return false;
            }
        }
        if (ha[0]!=0){
            return false;
        }
        return true;
    }
}
