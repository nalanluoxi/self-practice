package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：Test1129
 * @Date：2025/11/29 19:03
 * @Filename：Test1129
 */
public class Test1129 {
    public static void main(String[] args) {
        /*String[] strings = {"flower","flow","flight"};
        System.out.println(longestCommonPrefix(strings));*/

        /*String string = "  hello world  ";
        System.out.println("["+reverseWords(string)+"]");*/


       // System.out.println(strStr("sadbutsad","sad"));
        System.out.println(strStr("abc","c"));
    }

    public static String longestCommonPrefix(String[] strs) {
        if (strs.length==0){
            return "";
        }
        String ans=strs[0];
        for (String str : strs) {
            int len = Math.min(str.length(), ans.length());
            int index=0;
            while (index<len){
                if (ans.charAt(index)==str.charAt(index)){
                    index++;
                }else {
                    break;
                }
            }
        }
        return ans;
    }

    public static String reverseWords(String s) {
        String[] split = s.split(" ");
        String ans="";
        for (int i = split.length-1; i > 0; i--) {
            if (split[i].equals("")){
                continue;
            }
            ans+=split[i]+" ";
        }
        if (split[0].equals("")){
            return ans.substring(0,ans.length()-1);
        }
        ans+=split[0];
        return ans;
    }


    public static int canCompleteCircuit(int[] gas, int[] cost) {
        int sum=0;
        int allsum=0;
        int ans=0;
        for (int i = 0; i < gas.length; i++) {
            int now = gas[i] - cost[i];
            sum+=now;
            allsum+=now;
            if (sum<0){
                ans=i+1;
                sum=0;
            }
        }
        if (allsum<0){
            return -1;
        }
        return ans;
    }


    public static int strStr(String haystack, String needle) {
        int l1 = haystack.length();
        int l2 = needle.length();
        if (l1==0&&l2!=0||l1<l2){
            return -1;
        }
        if (l1==l2){
            return haystack.equals( needle)?0:-1;
        }
        for (int i = 0; i < l1; i++) {
            if (i+l2>l1){
                break;
            }
            String str = haystack.substring(i, i + l2);
            if (str.equals(needle)){
                return i;
            }
        }

        return -1;
    }
}
