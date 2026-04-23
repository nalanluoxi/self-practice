package luogu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class 分布式队列 {

    public static void main(String[] args) {
        fenbu();
    }


    public static void fenbu(){
        Scanner scanner=new Scanner(System.in);
        int N= scanner.nextInt();
        scanner.nextLine();
        List<List<Integer>> Arry=new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            Arry.add(new ArrayList<>());
        }

        while (scanner.hasNext()){
            String com=scanner.nextLine();
            //增加
            if (com.charAt(0)=='a'){
                String[] s = com.split("\\s+");
                int num= Integer.parseInt(s[1]);
                Arry.get(0).add(num);
             //   System.out.println("0号添加数据:"+num);
            }
            //查询
            else if (com.charAt(0)=='q') {
                int count=Arry.get(0).size();
                for (int i = 1; i < N; i++) {
                    count=Math.min(count,Arry.get(i).size());
                }

                System.out.println(count);
            }
            //更新
            else if (com.charAt(0)=='s') {
                String[] s = com.split("\\s+");
                int index= Integer.parseInt(s[1]);
                List<Integer> tarlist = Arry.get(index);
                int sizetarlist = tarlist.size();
                if (sizetarlist<Arry.get(0).size()){
                    tarlist.add(Arry.get(0).get(sizetarlist));
                }

            }

        }

    }


}
