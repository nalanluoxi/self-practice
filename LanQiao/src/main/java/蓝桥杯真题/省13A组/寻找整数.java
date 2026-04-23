package 蓝桥杯真题.省13A组;


import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.省13A组
 * @Project：LanQiaoBei
 * @name：寻找整数
 * @Date：2025/4/2 16:27
 * @Filename：寻找整数
 */
public class 寻找整数 {
    public static void main(String[] args) {

        System.out.println("1368799");
        //System.out.println(getmax(42));
       /* long n=2*3*5*7*11*13*17*19*23*27*29*31*37*41*43*47;
        System.out.println(n);
        //System.out.println(getmax);
        long max=100000000000000000L;
        System.out.println(max);*/
        test1();
        /*List<Long> longs = test1();
        System.out.println(longs.size());
        System.out.println("答案："+longs.get(0));
    */    // System.out.println(test1());
        //奇数
        //11 的倍数
    }


    public static /*List<Long>*/void test1(){
        List<Long> ans=new ArrayList<>();
      long min=511930726l;
          long max=100000000000000000L;
        for (long l = min; l < max; l++) {
            if (l%2!=1){
                continue;
            }
            if (l%11!=0){
                continue;
            }
            if (l%17!=0){
                continue;
            }
            //System.out.println("满足条件的："+l);
            ans.add(l);
        }
        System.out.println("第一阶段过滤完成");
        System.out.println("第一阶段大小:"+ans.size());
        test(ans);
       // return ans;
    }

    public static/* List<Long>*/void test(List<Long> list){
      //  long max=100000000000000000L;
        List<Long> ans=new ArrayList<>();
        //for (long l = 0; l < max; l++) {
        System.out.println("第二阶段过滤");
        for(long l:list){
            if (l%3!=2){
                continue;
            }
            if (l%4!=1){
                continue;
            }
            if (l%5!=4){
                continue;
            }
            if (l%6!=5){
                continue;
            }
            if (l%2!=1){
                continue;
            }
            if (l%11!=0){
                continue;
            }
            if (l%17!=0){
                continue;
            }
            if (l%3!=2){
                continue;
            }
            if (l%4!=1){
                continue;
            }
            if (l%5!=4){
                continue;
            }
            if (l%6!=5){
                continue;
            }
            if (l%7!=4){
                continue;
            }
            if (l%8!=1){
                continue;
            }
            if (l%9!=2){
                continue;
            }
            if (l%10!=9){
                continue;
            }

            if (l%12!=5){
                continue;
            }
            if (l%13!=10){
                continue;
            }
            if (l%14!=11){
                continue;
            }
            if (l%15!=14){
                continue;
            }
            if (l%16!=9){
                continue;
            }

            if (l%18!=11){
                continue;
            }
            if (l%19!=18){
                continue;
            }
            if (l%20!=9){
                continue;
            }
            if (l%21!=11){
                continue;
            }
            if (l%22!=11){
                continue;
            }
            if (l%23!=15){
                continue;
            }
            if (l%24!=15){
                continue;
            }
            if (l%25!=9){
                continue;
            }
            if (l%26!=23){
                continue;
            }
            if (l%27!=20){
                continue;
            }
            if (l%28!=25){
                continue;
            }
            if (l%29!=16){
                continue;
            }
            System.out.println("满足条件的:"+l);
            ans.add(l);
        }
        System.out.println("第二阶段大小"+ans.size());
        test2(ans);

        //return ans;
    }

    public static List<Long> test2(List<Long> list){
        System.out.println("");
        System.out.println("第三阶段过滤");
        List<Long> ans=new ArrayList<>();
        for (long l:list){
            if (l%30!=29){
                continue;
            }
            if (l%31!=27){
                continue;
            }
            if (l%32!=25){
                continue;
            }
            if (l%33!=11){
                continue;
            }
            if (l%34!=17){
                continue;
            }
            if (l%35!=4){
                continue;
            }
            if (l%36!=29){
                continue;
            }
            if (l%37!=22){
                continue;
            }
            if (l%38!=37){
                continue;
            }
            if (l%39!=23){
                continue;
            }
            if (l%40!=9){
                continue;
            }
            if (l%41!=1){
                continue;
            }
            if (l%42!=11){
                continue;
            }
            if (l%43!=11){
                continue;
            }
            if (l%44!=33){
                continue;
            }
            if (l%45!=29){
                continue;
            }
            if (l%46!=15){
                continue;
            }
            if (l%47!=5){
                continue;
            }
            if (l%48!=41){
                continue;
            }
            if (l%49!=46){
                continue;
            }
            System.out.println("满足条件的"+l);
            ans.add(l);
        }
        System.out.println("第三阶段结果");
        System.out.println(ans.size());
       // System.out.println(ans.get(0));
        return ans;
    }



}
