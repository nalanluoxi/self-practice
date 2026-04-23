package likou;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：全排列
 * @Date：2025/2/3 16:49
 * @Filename：全排列
 */
public class 全排列 {
    public static void main(String[] args) {
        List<List<Integer>> permute = permute(new int[]{1, 2, 3,4,5,6,7,8,9,10,11,12,13,14});
        for (List<Integer> integers : permute) {
            for (Integer integer : integers) {
                System.out.print(integer+",");
            }
            System.out.println();
        }
    }

    static List<List<Integer>> res;
    static List<Integer> list;
    public static List<List<Integer>> permute(int[] nums) {
        res=new ArrayList<>();
        list=new ArrayList<>();
        for (int num : nums) {
            list.add(num);
        }
        dps(0);
        return res;
    }
    public static void swap(int a,int b){
        Integer temp = list.get(a);
        list.set(a,list.get(b));
        list.set(b,temp);
    }
    public static void dps(int index){
        if (index==list.size()-1){
           /* if (isOk()){
                System.out.println(list);
                res.add(new ArrayList<>(list));
            }*/
            res.add(new ArrayList<>(list));

            // printAll(list);
            return;
        }
        for (int i = index; i <list.size() ; i++) {
           swap(i,index);
           dps(index+1);
           swap(i,index);
        }
    }

    public static boolean isOk(){
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i)==i+1){
                return false;
            }
        }
        return true;
    }

    public static void printAll(List<Integer> list){
        System.out.print("[");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i));
            if (i==list.size()-1){
                System.out.println("]");
            }else {
                System.out.print(",");
            }
        }
    }




}
