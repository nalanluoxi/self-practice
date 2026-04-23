package 蓝桥杯真题.十四届国赛;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.十四届国赛
 * @Project：LanQiaoBei
 * @name：栈
 * @Date：2025/6/14 20:13
 * @Filename：栈
 */
public class 栈 {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        Integer n = Integer.valueOf(scanner.nextLine());
        List<Integer> list = new LinkedList<>();
        Set<Integer> set = new HashSet<>();
        for (Integer i = n; i > 0; i--) {
            Integer temp = Integer.valueOf(scanner.nextLine());
            if (set.contains(temp)){
                list.remove(temp);
                list.add(temp);
            }else {
                list.add(temp);
                set.add(temp);
            }
            print(list);
        }
    }

    public static void print(List<Integer> list){
        int n=0;
        for(int i=1;i<list.size();i++){
            if ((list.get(i)+list.get(i-1))%2!=0){
                n++;
            }
        }
        System.out.println(n);
    }
}

