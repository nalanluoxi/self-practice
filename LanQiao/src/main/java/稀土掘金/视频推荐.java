package 稀土掘金;

import java.sql.SQLOutput;
import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：视频推荐
 * @Date：2025/2/7 9:57
 * @Filename：视频推荐
 */
public class 视频推荐 {



    public static void main(String[] args) {
        //  You can add more test cases here
        System.out.println(solution("10,1,9,2,8,3,7,4,6,5") == 8);
        System.out.println(solution("5,3,9,1,7") == 7);
        System.out.println(solution("2,15,3,16,1,3,13,12,4,6,2") == 13);
        System.out.println(solution("1,0,8,7,3,9,12,6,4,15,17,2,14,5,10,11,19,13,16,18") == 15);
        System.out.println(solution("76,100,5,99,16,45,18,3,81,65,102,98,36,4,2,7,22,66,112,97,68,82,37,90,61,73,107,104,79,14,52,83,27,35,93,21,118,120,33,6,19,85,49,44,69,53,67,110,47,91,17,55,80,78,119,15,11,70,103,32,9,40,114,26,25,87,74,1,30,54,38,50,8,34,28,20,24,105,106,31,92,59,116,42,111,57,95,115,96,108,10,89,23,62,29,109,56,58,63,41,77,84,64,75,72,117,101,60,48,94,46,39,43,88,12,113,13,51,86,71") == 96);
    }

    public static int solution(String data) {
        // Please write your code here
        String[] split = data.split(",");
        int[]list=new int[split.length];
        for (int i = 0; i < split.length; i++) {
            list[i]=Integer.valueOf(split[i]);
        }
        Arrays.sort(list);
        System.out.print("[");
        for (int i = 0; i < list.length; i++) {
            System.out.print(list[i]+",");
        }
        System.out.println("]");
        int index= (int) (list.length*0.8);
        System.out.println("原本index："+index);
        if (list.length%2==0){
           // System.out.println("偶数:"+index);
            index--;
        }
        //index++;
        System.out.println("list["+index+"]="+list[index]);
        //index--;
        return list[index];
    }
}
