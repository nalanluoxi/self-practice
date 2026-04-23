package luogu;

import java.util.Scanner;

public class 脱落种子 {
    public static void main(String[] args) {
        zhongzi();
    }

    public static void zhongzi() {
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        System.out.println(getcount(str));
    }

    public static int getcount(String str) {
        char[] arr = str.toCharArray();
        int start = 0;
        int end = arr.length - 1;
        int count=0;
        while (start<=end){
            if (arr[start]!=arr[end]){
                count++;
                boolean flag=true;
                for (int i=end-1;i>start;i--){
                    if (arr[i]==arr[start]){
                        end--;
                        flag=false;
                        break;
                    }
                }
                if (flag==true){
                    start++;
                }
            }else {
                start++;
                end--;
            }
        }
        return count;
    }



}
