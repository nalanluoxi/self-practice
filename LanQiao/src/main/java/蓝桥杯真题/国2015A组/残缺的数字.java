package 蓝桥杯真题.国2015A组;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.国2015A组
 * @Project：LanQiaoBei
 * @name：残缺的数字
 * @Date：2025/3/25 10:21
 * @Filename：残缺的数字
 */
public class 残缺的数字 {
    public static void main(String[] args) {
        int sum=1;
        String n = "0000011";sum*=help(n);
        n = "1001011";sum*=help(n);
        n = "0000001";sum*=help(n);
        n = "0100001";sum*=help(n);
        n = "0101011";sum*=help(n);
        n = "0110110";sum*=help(n);
        n = "1111111";sum*=help(n);
        n = "0010110";sum*=help(n);
        n = "0101001";sum*=help(n);
        n = "0010110";sum*=help(n);
        n = "1011100";sum*=help(n);
        n = "0100110";sum*=help(n);
        n = "1010000";sum*=help(n);
        n = "0010011";sum*=help(n);
        n = "0001111";sum*=help(n);
        n = "0101101";sum*=help(n);
        n = "0110101";sum*=help(n);
        n = "1101010";sum*=help(n);
        System.out.println("sum:  "+sum);
        System.out.println("254016000");
    }

    public static int help(String n) {
        init();
        Set<Integer> integers = numbers(n);
        System.out.println(integers);
        System.out.println(integers.size());
        if (integers.size() == 0) {
            System.out.println("size==0  num:" + n);
        }
        return integers.size();
    }

    public static Set<Integer> numbers(String string) {
        Set<Integer> ans = new HashSet<>();
        if (string.equals("1111111")) {
            ans.add(8);
            return ans;
        }

        if (ZeroNum(string) == 0) {
            return ans;
        }
        char[] charArray = string.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == '0') {
                charArray[i] = '1';
                String nstr = String.valueOf(charArray);
                if (map.containsKey(nstr)) {
                    ans.add(map.get(nstr));
                }
                Set<Integer> numbers = numbers(nstr);
                ans.addAll(numbers);
                charArray[i] = '0';
            }
        }
        return ans;
    }

    public static int ZeroNum(String str) {
        int ans = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '0') {
                ans++;
            }
        }
        return ans;
    }

    static HashMap<String, Integer> map;

    public static void init() {
        map = new HashMap<>();
        map.put("1111110", 0);
        map.put("0110000", 1);
        map.put("1101101", 2);
        map.put("1111001", 3);
        map.put("0110011", 4);
        map.put("1011011", 5);
        map.put("1011111", 6);
        map.put("1110000", 7);
        map.put("1111111", 8);
        map.put("1111011", 9);
    }
}
