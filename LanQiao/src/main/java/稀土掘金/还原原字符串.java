package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：还原原字符串
 * @Date：2025/1/19 15:44
 * @Filename：还原原字符串
 */
public class 还原原字符串 {


    public static void main(String[] args) {
        // Add your test cases here

        System.out.println(solution("abbabbbabb").equals("ab"));
    //   System.out.println(solution("abbbabbbb").equals("ab"));
       // System.out.println(solution("jiabanbananananiabanbananananbananananiabanbananananbananananbananananbanananan").equals("jiaban"));
       // System.out.println(solution("selectecttectelectecttectcttectselectecttectelectecttectcttectectelectecttectcttectectcttectectcttectectcttect").equals("select"));
     //   System.out.println(solution("discussssscussssiscussssscussssdiscussssscussssiscussssscussssiscussssscussss").equals("discus"));
    }




    public static String solution(String str1) {
        // Edit your code here
        for (int i = 1; i <= str1.length(); i++) {
            String tempStr = str1.substring(0, i);
            if (isOk(tempStr,str1)){
                return tempStr;
            }
        }
        return str1;
    }


    public static boolean isOk(String string ,String target){

        String tempstr="";
        while (tempstr.length()<=target.length()) {
            for (int k = 0; k <= string.length(); k++) {
                String s = string + string.substring(k);

            }

        }

        return true;
    }

}
