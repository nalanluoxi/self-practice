package likou;

import java.util.ArrayList;
import java.util.List;

public class 字母异位分词组 {
    public static void main(String[] args) {
        String[] str = {"eat", "tea", "tan", "ate", "nat", "bat"};
        List<List<String>> lists = groupAnagrams(str);
        for (List<String> list : lists) {
            System.out.print("[ ");
            for (String string : list) {
                System.out.print(string + " ");
            }
            System.out.print("] , ");
        }
       /* String a="a";
        int[] ints = new int[26];
        System.out.println(ints[a.charAt(0)-97]);
        ints[a.charAt(0)-97]++;
        System.out.println(ints[a.charAt(0)-97]);*/
    }

    public static List<List<String>> groupAnagrams(String[] strs) {
        List<List<String>> res = new ArrayList<>();
        for (String str : strs) {
            int[] words = getWords(str);
            int index = isIn(res, words);
            if (index == -1) {
                List<String> newlist = new ArrayList<>();
                newlist.add(str);
                res.add(newlist);
            } else {
                res.get(index).add(str);
            }
        }
        return res;
    }

    public static int isIn(List<List<String>> res, int[] words) {
        for (int index = 0; index < res.size(); index++) {
            List<String> tem = res.get(index);
            String string = tem.get(0);
            int[] reswords = getWords(string);
            if (equal(reswords, words)) {
                return index;
            }
        }
        return -1;
    }

    public static boolean equal(int[] w1, int[] w2) {
        for (int i = 0; i < w1.length; i++) {
            if (w1[i] != w2[i]) {
                return false;
            }
        }
        return true;
    }

    public static int[] getWords(String string) {
        int[] words = new int[26];
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            words[c - 97]++;
        }
        return words;
    }
}
