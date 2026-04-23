package example.java2;

import likou.删除倒数第n个;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2
 * @Project：LanQiaoBei
 * @name：最长有效子串
 * @Date：2025/6/29 19:11
 * @Filename：最长有效子串
 */
public class 最长有效子串 {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Map<Integer,Integer>map=new HashMap<>();
        String[] str1 = scanner.nextLine().split(" ");
        String[] str2 = scanner.nextLine().split(" ");
        for (int i = 0; i < str1.length; i++) {
            map.put(i+1,Integer.valueOf(str1[i]));
        }
        List<Map.Entry<Integer, Integer>> collect = map.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList());
        int size = collect.size();
        int all=0;
        for (int i = 0; i < size / 2; i++) {
            Integer value = collect.get(i).getValue();
            all+=value;
        }
        System.out.println(all);

    }
 /*   public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        int n = in.nextInt();
        HashMap<Integer,LinkedList<Integer>>map=new HashMap<>();
        for (int i = 0; i < n; i++) {
            int a = in.nextInt();
            int b = in.nextInt();
            if (!map.containsKey(b)){
                map.put(b,new LinkedList<>());
            }
            map.get(b).addLast(i+1);
        }
        map.keySet().stream().sorted().forEach(key->{
            LinkedList<Integer> list = map.get(key);
            list.forEach(value->{
                System.out.print(value+" ");
            });
        });
    }
*/


    /* public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String string = in.nextLine();
        System.out.println(help(string));
    }
    public static int help(String s) {
        char[] list = s.toCharArray();
        int n = list.length;
        if (n==0||n==1){
            return 0;
        }
        int []dp=new int[n];
        int max=0;
        for (int i = 0; i < n; i++) {
            if (list[i]=='0'){
                dp[i]=0;
            }else {
                if (i-1<0){
                    dp[i]=0;
                    continue;
                }
                int be=dp[i-1];
                if (i-be-1<0||list[i-be-1]=='1'){
                    dp[i]=0;
                    continue;
                }
                dp[i]=2+dp[i-1];
                if (i-be-2>=0){
                    dp[i]+=dp[i-be-2];
                }
            }
            max=Math.max(max,dp[i]);
        }
        return max;
    }*/
   /* public static int help(String s){
        int len = s.length();
        int []dp=new int[len+1];
        int ans=0;
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i-1)=='0'){
                dp[i]=0;
                continue;
            } else if (s.charAt(i-1)=='1') {
                if (i-1<0){
                    dp[i]=0;
                    continue;
                }
                int befor = dp[i - 1];
                if (i-befor-1<0|| s.charAt(i-befor-1)=='1'){
                    dp[i]=0;
                    continue;
                }
                dp[i]=2+dp[i-befor-2];
                if (i-befor-2>=0){
                    dp[i]+=dp[i-befor-2];
                }
            }
            ans=Math.max(ans,dp[i]);
        }
        return ans;
    }*/

   /* public static String help(String s){

        char[] array = s.toCharArray();
        int []dp=new int[array.length+1];
        String ans="";
        for (int i = 0; i < array.length; i++) {
            if (isOk(array[i])){
                dp[i+1]=dp[i]+1;
            }else {
                dp[i+1]=0;
            }
            if (dp[i]>dp[i+1]){
                ans=s.substring(i-dp[i],i);
            }
        }

        return ans;
    }

    public static boolean isOk(Character c){
        if (c>='0'&&c<='9'){
            return true;
        } else if (c >= 'a' && c <= 'z') {
            return true;
        }else if (c >= 'A' && c <= 'Z') {
            return true;
        }else if (c=='_'){
            return true;
        }
        return false;
    }*/
}
