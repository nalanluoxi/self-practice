package likou;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：复原ip
 * @Date：2025/4/3 17:17
 * @Filename：复原ip
 */
public class 复原ip {
    public static void main(String[] args) {
        List<String> list1 = restoreIpAddresses("101023");
        List<String> list = restoreIpAddresses("25525511135");
        for (String s : list) {
            System.out.println(s);
        }
    }


    static List<String> list;
    static String temp;

    public static List<String> restoreIpAddresses(String s) {
        list = new ArrayList<>();
        temp = "";
        if (s.length() < 4 || s.length() > 12) {
            return list;
        }
        help(0, 0, s);
        return list;
    }

    public static void help(int index, int pointnum, String s) {
        if (pointnum == 3) {
            if (s.length() - index <= 3 && isTrue(s.substring(index))) {
                list.add( temp + s.substring(index));
            }
            return;
        }
        for (int i = index; i < Math.min(s.length(), index + 3); i++) {
            String t = s.substring(index, i + 1);
            if (!isTrue(t)) {
                continue;
            }
            temp +=  t+".";
            help(i+1, pointnum + 1, s);
            temp = temp.substring(0, temp.length() - 1 - t.length());
        }

    }

    public static boolean isTrue(String s) {
        if (s.length()<=0){
            return false;
        }
        if (s.length() > 3) {
            return false;
        }
        if (s.charAt(0) == '0' && s.length() > 1) {
            return false;
        }
        Integer num = Integer.valueOf(s);
        if (num > 255) {
            return false;
        }
        return true;
    }
}
