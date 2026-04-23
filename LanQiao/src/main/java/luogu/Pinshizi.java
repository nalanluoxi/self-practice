package luogu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Pinshizi {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        int n= Integer.parseInt(scanner.nextLine());
        List<List<Integer>> list=new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String num = scanner.nextLine();
            List<Integer>temnum=new ArrayList<>();
            for (String s:num.split(" ")){
                temnum.add(Integer.parseInt(s));
            }
            list.add(temnum);
        }
        int count=0;

        for (int i = 0; i < n; i++) {
            List<Integer> nowBox = list.get(i);
            Integer color = nowBox.get(2);
            count+=colorhelp(list,color,nowBox);
        }
        count=count/2;
        long mod=(long)1e9+7;
        count%=mod;

        System.out.println(count);

    }

    public static int colorhelp(List<List<Integer>> nums,int col,List<Integer> nowbox){
        int count=0;
        for (int i = 0; i < nums.size(); i++) {
            List<Integer> list = nums.get(i);
            if (!list.get(2).equals(col)){
                if ((nowbox.get(0)>list.get(0)&&nowbox.get(1)<list.get(1))||
                        (nowbox.get(0)<list.get(0)&&nowbox.get(1)>list.get(1))){
                    count++;
                }
            }
        }
        return count;
    }
}
