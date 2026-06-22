package likou.力扣test2;

import likou.entity.TreeNode;

import java.util.List;
import java.util.*;

/**
 * @author nalan_luoxi
 * @version 1.0
 * @email adrian0304@qq.com
 * @project self-practice
 * @package likou.力扣test2
 * @date 2026-06-15 09:28
 */
public class Test0615 {

    public static void main(String[] args) {
        System.out.println(letterCombinations("23"));
    }



    public static int minDepth(TreeNode root) {
        return dfs2(root);
    }

    public static int dfs2(TreeNode root){
        if (root==null){
            return 0;
        }
        if (root.left == null) {
            return dfs2(root.right)+1;
        }
        if (root.right==null){
            return dfs2(root.left)+1;
        }
        return Math.min(dfs2(root.left),dfs2(root.right))+1;
    }

    public static List<String> letterCombinations(String nums) {

        String[] map = {
                "",
                "",
                "abc",
                "def",
                "ghi",
                "jkl",
                "mno",
                "pqrs",
                "tuv",
                "wxyz"
        };
        List<String> ans=new ArrayList<>();
        List<String> brr=new ArrayList<>();
        if (nums.equals("")){
            return ans;
        }
        for (int i = 0; i < nums.length(); i++) {
            brr.add(map[nums.charAt(i)-'0']);
        }
        dfs1(0,"",brr,ans);
        return ans;
    }

    public static void dfs1(int index,String string,List<String> brr,List<String>ans){
        if (index==brr.size()){
            ans.add(new String(string));
            return;
        }
        String temp = brr.get(index);
        for (int i = 0; i < temp.length(); i++) {
            dfs1(index+1,string+temp.charAt(i),brr,ans);
        }
    }
}
