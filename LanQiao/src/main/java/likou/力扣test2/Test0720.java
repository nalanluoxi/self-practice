package likou.力扣test2;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：Test0720
 * @Date：2025/7/20 10:03
 * @Filename：Test0720
 */
public class Test0720 {

    public static void main(String[] args) {
/*        Trie trie=new Trie();
        trie.insert("apple");
        trie.search("apple");*/
        Queue<Integer> queue=new PriorityQueue<>();
        queue.add(1);
        queue.add(2);
        queue.add(3);
        System.out.println(queue.poll());
        //System.out.println(longestValidParentheses(")()())"));
    }


    static class MedianFinder {

        Queue<Integer> min;
        Queue<Integer> max;

        public MedianFinder() {
            min=new PriorityQueue<>();
            max=new PriorityQueue<>((a,b)->b-a);
        }

        public void addNum(int num) {
            if (max.size()==min.size()){
                min.add(num);
                max.add(min.poll());
            }else {
                max.add(num);
                min.add(max.poll());
            }
        }

        public double findMedian() {
            if (min.size()==max.size()){
                return (min.peek()+max.peek())*2.0;
            }
            return max.peek();
        }
    }
    public static int longestValidParentheses(String s) {
        int len = s.length();
        if (len<=1){
            return 0;
        }
        int ans=0;
        int[]dp=new int[len];
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c=='('){
                dp[i]=0;
            }else if (c==')'){
                if (i==0){
                    dp[i]=0;
                    continue;
                }
                int befor = dp[i - 1];
                if (i-befor-1<0|| s.charAt(i-befor-1)==')'){
                    dp[i]=0;
                }else {
                    dp[i] += dp[i - 1] + 2;
                    if (i - befor - 2 >= 0) {
                        dp[i] += dp[i - befor - 2];
                    }
                }
            }
            ans=Math.max(ans,dp[i]);
        }
        return ans;
    }

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
                char c = word.charAt(i);
                int index = c - 'a';
                if (node.children[index]==null){
                    node.children[index]=new Trie();
                }
                node=node.children[index];
            }
            node.isEnd=true;
        }

        public boolean search(String word) {
            Trie trie = searchPre(word);
            return trie!=null&& trie.isEnd ;
        }


        public Trie searchPre(String pre){
            Trie node = this;
            for (int i = 0; i < pre.length(); i++) {
                char c = pre.charAt(i);
                int index = c - 'a';
                if (node.children[index]==null){
                    return null;
                }
                node=node.children[index];
            }
            return node;
        }
        public boolean startsWith(String prefix) {
            return searchPre(prefix)!=null;
        }
    }



}
