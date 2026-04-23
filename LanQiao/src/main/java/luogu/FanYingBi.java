package luogu;

import java.util.Scanner;

public class FanYingBi {

    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        String now= scanner.next();
        String target= scanner.next();

        int count=0;
        for (int i = 0; i < now.length(); i++) {
            String n1 = now.substring(i, i + 1);
            String t1 = target.substring(i, i + 1);
            if (!n1.equals(t1)){
                now=swap(i,now);
                now=swap(i+1,now);
                count++;
            }
        }
        System.out.println(count);

    }

    public static String swap(int index,String str){
        if (str.substring(index)==null){
            return str;
        }
        if (str.substring(index,index+1).equals("o")){
            return str.substring(0,index)+"*"+str.substring(index+1);
        } else if (str.substring(index,index+1).equals("*")) {
            return str.substring(0,index)+"o"+str.substring(index+1);
        }
        return str;
    }
}
