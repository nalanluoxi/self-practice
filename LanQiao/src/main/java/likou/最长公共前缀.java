package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：最长公共前缀
 * @Date：2025/5/10 20:28
 * @Filename：最长公共前缀
 */
public class 最长公共前缀 {
    public static void main(String[] args) {
        System.out.println(longestCommonPrefix(new String[]{"flower", "flow", "flight"}));
    }

    public static String longestCommonPrefix(String[] strs) {
        if (strs.length == 0) {
            return "";
        }
        String ans = strs[0];
        for (int i = 0; i < strs.length; i++) {
            String str = strs[i];
            int len=Math.min(ans.length(),str.length());
            int index=0;
            while (index<len && ans.charAt(index)==str.charAt(index)){
                index++;
            }
            ans=ans.substring(0,index);
        }
        return ans;
    }
}
