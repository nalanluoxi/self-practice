package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：比较版本号
 * @Date：2025/4/25 16:28
 * @Filename：比较版本号
 */
public class 比较版本号 {
    public static void main(String[] args) {
        System.out.println(compareVersion("1.2","1.10"));
        System.out.println(compareVersion("1.001","1.000001"));
        System.out.println(compareVersion("1.001","1.000001.0.0.0.000"));
        /*String[] split = "1.2".split("\\.");
        for (String s : split) {
            System.out.println(s);
        }*/
    }

    public static int compareVersion(String version1, String version2) {
        String[] str1 = version1.split("\\.");
        int len1 = str1.length;
        String[] str2 = version2.split("\\.");
        int len2 = str2.length;
        int max = Math.max(len1, len2);
        int i=0;
        while(i<max) {
            int num1 =i>=len1? 0 : Integer.parseInt(str1[i]);
            int num2 =i>=len2? 0: Integer.parseInt(str2[i]);
            if (num1>num2){
                return 1;
            }else if (num1<num2){
                return -1;
            } else if (num1==num2) {
                i++;
            }
        }
        return 0;
    }
}
