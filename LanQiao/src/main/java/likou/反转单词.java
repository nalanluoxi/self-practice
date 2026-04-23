package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：反转单词
 * @Date：2025/2/14 10:24
 * @Filename：反转单词
 */
public class 反转单词 {
    public static void main(String[] args) {
        System.out.println(reverseWords("a good   example"));
    }

    public static String reverseWords(String s) {
        String ans="";
        String[] split = s.split(" ");
        for (int i = split.length-1; i >0 ; i--) {
            if(split[i].equals("")){
                continue;
            }
            ans+=split[i]+" ";
        }
        if(split[0].equals("")){
            return ans.substring(0,ans.length()-1);
        }
        ans+=split[0];
        return ans;
    }
}
