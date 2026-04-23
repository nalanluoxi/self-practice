package likou.力扣test2;

import likou.entity.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：Test0817
 * @Date：2025/8/17 16:25
 * @Filename：Test0817
 */
public class Test0817 {

    public static void main(String[] args) {



/*
        List<String> list1 = letterCombinations("");
        for (String string : list1) {
            System.out.println(string);
        }*/
        //List<List<Integer>> permute = permute(new int[]{1, 2, 3});
        List<String> list1 = generateParenthesis(3);
        for (String string : list1) {
            System.out.println(string);
        }
    }
    public static List<String> generateParenthesis(int n) {

        List<String> ans=new ArrayList<>();
        char[] path=new char[n*2];
        kuo(n,0,0,path,ans);
        return ans;
    }

    public static void kuo(int n,int i,int left,char[] path,List<String> ans){
       if (i==n*2){
           ans.add(new String(path));
           return;
       }
       if (left<n){
           path[i]='(';
           kuo(n,i+1,left+1,path,ans);
       }if (i-left<left){
            path[i]=')';
            kuo(n,i+1,left,path,ans);
       }
    }
    public static List<List<Integer>> combinationSum(int[] nums, int target) {
        Arrays.sort(nums);
        List<List<Integer>> ans=new ArrayList<>();
        List<Integer> tans=new ArrayList<>();
        zuhe(nums,target,0,tans,ans);
        return ans;
    }

    public static void zuhe(int[]nums,int target,int start,List<Integer> tans,List<List<Integer>>ans){
        if (target==0){
            ans.add(new ArrayList<>(tans));
            return;
        }
        for (int i = start; i < nums.length; i++) {
            if (nums[i]>target){
                return;
            }
            if (i > start && nums[i] == nums[i-1]) {
                continue;
            }
            tans.add(nums[i]);
            zuhe(nums,target-nums[i],i,tans,ans);
            tans.remove(tans.size()-1);
        }
    }

    static String[] map={
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
    public static List<String> letterCombinations(String digits) {

        List<String>ans=new ArrayList<>();
        List<String> brr =new ArrayList<>();
        if (digits.equals("")){
            return ans;
        }
        for (int i = 0; i < digits.length(); i++) {
            int index = digits.charAt(i) - '0';
            brr.add(map[index]);
        }
        phone(brr,0,"",ans);
        return ans;
    }

    public static void phone(List<String> brr,int index,String string,List<String> ans){
        if (index==brr.size()){
            ans.add(new String(string));
            return;
        }
        String s = brr.get(index);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            phone(brr,index+1,string+c,ans);
        }
    }


    List<Integer> arr;
    public int kthSmallest(TreeNode root, int k) {
        arr = new ArrayList<>();
        getarr(root);
        return arr.get(k-1);
    }

    public void getarr(TreeNode root){
        if (root==null){
            return;
        }
        getarr(root.left);
        arr.add(root.val);
        getarr(root.right);
    }
    static List<Integer> list;
    public boolean isValidBST(TreeNode root) {
        list=new ArrayList<>();
        inorder(root);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i)<=list.get(i-1)){
                return false;
            }
        }
        return true;
    }

    public static void inorder(TreeNode root){
        if (root==null){
            return;
        }
        inorder(root.left);
        list.add(root.val);
        inorder(root.right);
    }
 /*   public boolean isValidBST(TreeNode root) {
        boolean b=true;
        if (root.left!=null){
            b=root.val>root.left.val && isValidBST(root.left);
        }
        if (root.right!=null){
            b= b && root.right.val>root.val && isValidBST(root.right);
        }
        return b;
    }*/

    static class Trie {

        private boolean isEnd;
        private Trie[] children;
        public Trie() {
            isEnd=false;
            children=new Trie[26];
        }

        public void insert(String word) {
            Trie node = this;
            for (int i = 0; i < word.length(); i++) {
                int index = word.charAt(i) - 'a';
                if (node.children[index]==null){
                    node.children[index]=new Trie();
                }
                node=node.children[index];
            }
            node.isEnd=true;
        }

        public Trie selectPre(String word){
            Trie node = this;
            for (int i = 0; i < word.length(); i++) {
                int index = word.charAt(i) - 'a';
                if (node.children[index]==null){
                    return null;
                }
                node=node.children[index];
            }
            return node;
        }

        public boolean search(String word) {
            Trie trie = selectPre(word);
            return trie!=null && trie.isEnd;
        }

        public boolean startsWith(String prefix) {
            return selectPre(prefix)!=null;
        }
    }


 /*   static List<List<Integer>> list ;
    static boolean b;
    static int[]visited;
    public boolean canFinish(int n, int[][] nums) {
        visited=new int[n];
        b=true;
        list=new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(new ArrayList<>());
        }
        for (int[] num : nums) {
            list.get(num[0]).add(num[1]);
        }
        for (int i = 0; i < n; i++) {
            if (visited[i]!=0){
                continue;
            }
            help(i);
            if (!b){
                return b;
            }
        }
        return b;
    }

    public static void help(int i){
        visited[i]=1;
        for (Integer integer : list.get(i)) {
            if (visited[integer]==1){
                b=false;
            }else if (visited[integer]==0){
                help(integer);
            }
            if (!b){
                return;
            }
        }
        visited[i]=2;
    }*/

    static List<List<Integer>>ans;
    static List<Integer>tans;

    public static List<List<Integer>> permute(int[] nums) {
        ans=new ArrayList<>();
        tans=new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            tans.add(nums[i]);
        }
        dfs(0);
        return ans;
    }

    public static void dfs(int index){
        if (index==tans.size()){
            ans.add(new ArrayList<>(tans));
            return;
        }
        for (int i = index; i < tans.size(); i++) {
            swap(i,index);
            dfs(index+1);
            swap(i,index);
        }
    }

    public static void swap(int i,int j){
        Integer p = tans.get(i);
        tans.set(i,tans.get(j));
        tans.set(j,p);
    }


}
