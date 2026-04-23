package pactice;

import java.util.Scanner;
import java.util.Arrays;
// 1:无需package
// 2: 类名必须Main, 不可修改

public class Saopdijiqiren {
    //设置初始值
    //用于存储总的街道长度，机器人数量
    static  int N,K;
    //用于存储机器人坐标
    static int[] robot;
    static int ans;

    public static void main(String[] args) {
        //输入数据
        Scanner in=new Scanner(System.in);
        //输入具体的数据
        N=in.nextInt();
        K=in.nextInt();
        robot=new int[K];
        //记录机器人位置
        for (int i=0;i<K;i++){
            robot[i]=in.nextInt();
        }
        //对数据进行排序
        Arrays.sort(robot);
        //处理数据
        //二分法找出最合适的机器人走的步数
        int left=1;
        int right=N;
        int middle=0;
        //当左右指针相遇即为答案
        while (right>left) {
            middle = (left+right)/2;
            //Check()函数检查所选的步数是否可以遍历整个街道
            if (Check(middle)) {
                //若合适则缩小机器人走的步数寻找更优值
                right = middle;
            }
            //若不合适则扩大机器人走的步数
            else {
                left=middle+1;
            }

        }
        ans=left;
        //答案为打扫的格子数-1=机器人走过的步数
        //乘2因为要回到初始点
        System.out.println((ans-1)*2);
    }
    private static boolean Check(int middle) {
        //表示当前机器人的上一个机器人所打扫的边界值
        int rightline=0;
        for (int i=0;i<K;i++){
            //当前机器人能到扫的左边界小于上个机器人的右边界
            //则表示有效
            if(robot[i]-middle<=rightline){
                //满足要求后分为两种情况
                //1.当前机器人在右边界之内
                if(robot[i]<=rightline){
                    rightline=robot[i]+middle-1;
                }
                else{
                    rightline+=middle;
                }
            }
            //若无效则证明所走的步数选择不合适
            else {
                return false;
            }
        }
        //表示已经扫完街道
        return rightline>=N;
    }
}
