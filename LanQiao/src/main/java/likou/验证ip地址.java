package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：验证ip地址
 * @Date：2025/6/1 19:29
 * @Filename：验证ip地址
 */
public class 验证ip地址 {

    public static void main(String[] args) {
/*        String string="172.16.254.1";
        String[] list = string.split("\\.");
        for (int i = 0; i < list.length; i++) {
            System.out.println(list[i]);
        }*/
       // System.out.println(validIPAddress("172.16.254.1"));
        //System.out.println(validIPAddress("2001:0db8:85a3:0:0:8A2E:0370:7334"));
       // System.out.println(validIPAddress("2001:0db8:85a3:0:0:8A2E:0370:7334:"));
        //System.out.println(validIPAddress("0.0.0.0"));
        System.out.println(validIPAddress("01.01.01.01"));
    }
    public static String validIPAddress(String queryIP) {
        if (queryIP.indexOf(".") >= 0 && isIPv4(queryIP)) return "IPv4";
        if (queryIP.indexOf(":") >= 0 && isIPv6(queryIP)) return "IPv6";
        return "Neither";
    }



    private static boolean isIPv4(String ip) {
        String[] ips = ip.split("\\.", -1); // 使用-1保留空字符串
        if (ips.length != 4) return false;
        for (String s : ips) {
            if (s.length() == 0 || s.length() > 3) return false;
            if (s.length() > 1 && s.charAt(0) == '0') return false;
            for (char c : s.toCharArray()) if (!Character.isDigit(c)) return false;
            int num = Integer.parseInt(s);
            if (num > 255) return false;
        }
        return true;
    }
    private static boolean isIPv6(String ip) {
        String[] ips = ip.split(":", -1);
        if (ips.length != 8) return false;
        for (String s : ips) {
            if (s.length() == 0 || s.length() > 4) return false;
            for (char c : s.toCharArray()) {
                if (!Character.isDigit(c) && Character.toLowerCase(c) > 'f') return false;
            }
        }
        return true;
    }

}
