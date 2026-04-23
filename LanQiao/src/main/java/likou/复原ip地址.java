package likou;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：复原ip地址
 * @Date：2025/2/12 18:25
 * @Filename：复原ip地址
 */
public class 复原ip地址 {
    public static void main(String[] args) {
        List<String> list = restoreIpAddresses("101023");
        for (String s : list) {
            System.out.println(s);
        }
    }

    static List<String> ans;
    static String tans;

    public static List<String> restoreIpAddresses(String s) {
        if (s.length() < 4 || s.length() > 12) {
            return new ArrayList<>();
        }
        ans = new ArrayList<>();
        tans = "";
        backtracking(s, 0, 0);
        return ans;
    }

    public static void backtracking(String s, int index, int point) {
        if (point == 3) {
            if ( index < s.length()&&isIp(s.substring(index))) {
                ans.add(tans + s.substring(index, s.length()));
            }
            return;
        }
        for (int i = index; i < Math.min(s.length(), index + 3); i++) {
            String temp = s.substring(index, i + 1);
            if (!isIp(temp)) {
                continue;
            }
            tans += temp + ".";
            backtracking(s, i + 1, point + 1);
            tans = tans.substring(0, tans.length() - temp.length() - 1);
        }

    }

    public static boolean isIp(String s) {
        if (s.length() == 1) {
            return true;
        }
        if (s.charAt(0) == '0') {
            return false;
        }
        if (s.length() > 3) {
            return false;
        }
        int num = Integer.parseInt(s);
        if (num > 255) {
            return false;
        }
        return true;
    }
}
