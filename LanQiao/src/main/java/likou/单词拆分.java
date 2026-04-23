package likou;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：单词拆分
 * @Date：2025/2/9 19:14
 * @Filename：单词拆分
 */
public class 单词拆分 {
    public static void main(String[] args) {
        //System.out.println(wordBreak("leetcode",List.of("leet","code")));
        //System.out.println(wordBreak("applepenapple",List.of("apple","pen")));
/*        List<String> list = new ArrayList<>();
        list.add("car");
        list.add("ca");
        list.add("rs");*/
       // System.out.println(wordBreak("aaaaaaa",List.of("aaaa","aaa")));
        System.out.println(wordBreak("catsanddog",List.of("cats","dog","sand","and","cat")));
    }

    static int maxlen=0;
    static Set<String> set;

    static int visited[];
    public static boolean wordBreak(String s, List<String> w) {
        set = new HashSet<>(w);
        visited = new int[s.length()+1];
        for (String s1 : w) {
            maxlen = Math.max(maxlen, s1.length());
        }
        Arrays.fill(visited, -1);
        return dfs(s, s.length())==1;
        //return true;
    }
    public static int dfs(String s, int i) {
        if (i==0 ){
            return 1;
        }
        if (visited[i]!=-1){
            return visited[i];
        }
        for (int j = i-1; j >=Math.max(0,i-maxlen) ; j--) {
            if (set.contains(s.substring(j,i))&&dfs(s,j)==1){
               return visited[i]=1;
            }
        }
        return visited[i]=0;
    }

}
