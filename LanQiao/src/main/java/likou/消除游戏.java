package likou;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：消除游戏
 * @Date：2025/2/8 9:07
 * @Filename：消除游戏
 */
public class 消除游戏 {

    public static void main(String[] args) {
        System.out.println(lastRemaining(9));
    }
    public static int lastRemaining(int n) {
        List<Integer>list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(i+1);
        }
        while (list.size()!=1){
            list=removeList(list,true);
            if (list.size()!=1){
                list=removeList(list,false);
            }
        }
        return list.get(0);
    }

    public static List<Integer> removeList(List<Integer>list,boolean isLTR){
        List<Integer> ans = new ArrayList<>();
        if (isLTR){
            for (int i = 1; i < list.size(); i+=2) {
                ans.add(list.get(i));
            }
        }else {
            for (int i = list.size()-2; i >=0; i-=2) {
                ans.add(list.get(i));
            }
        }
        return ans;
    }

}
