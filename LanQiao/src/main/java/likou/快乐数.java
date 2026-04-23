package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：快乐数
 * @Date：2025/2/2 16:30
 * @Filename：快乐数
 */
public class 快乐数 {
    public static void main(String[] args) {
        System.out.println(isHappy(19));
    }

    public static boolean isHappy(int n) {
        int count=0;
        while (n!=1){
            n=getNum(n);
            count++;
            if (count==100){
                return false;
            }
        }
        return true;
    }

    public static int getNum(int num) {
        int sum = 0;
        int temp=num%10;
        while (num!=0){
            sum+=temp*temp;
            num=num/10;
            temp=num%10;
        }
        System.out.println(sum);
        return sum;
    }
}
