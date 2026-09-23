package likou.力扣test2;

import likou.entity.ListNode;

import java.util.ArrayList;
import java.util.List;

public class Test260909 {


    public static void main(String[] args) {

    }


    public static void setZeroes(int[][] matrix) {
        List<Integer[]> list=new ArrayList<>();
        int len = matrix.length;
        int wid = matrix[0].length;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < wid; j++) {
                if (matrix[i][j]==0){
                    list.add(new Integer[]{i,j});
                }
            }
        }
        for (Integer[] integers : list) {
            Integer x = integers[0];
            Integer y = integers[1];
            for (int i = 0; i < wid; i++) {
                matrix[x][i]=0;
            }
            for (int i = 0; i < len; i++) {
                matrix[i][y]=0;
            }
        }

    }


    public static int minDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();
        if (len2*len1==0){
            return len1+len2;
        }

        int[][]dp=new int[len1+1][len2+1];
        for (int i = 0; i <= len1; i++) {
            dp[i][0]=i;
        }
        for (int i = 0; i <= len2; i++) {
            dp[0][i]=i;
        }

        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {

                if (word1.charAt(i-1)==word2.charAt(j-1)){
                    dp[i][j]=dp[i-1][j-1];
                }else {
                    dp[i][j]=Math.min(dp[i-1][j-1],Math.min(dp[i-1][j],dp[i][j-1]))+1;
                }
            }
        }
        return dp[len1][len2];
    }

    public static ListNode mergeKLists(ListNode[] lists) {
        if (lists.length==0){
            return null;
        }
        if (lists.length==1){
            return lists[0];
        }
        return merge(lists,0, lists.length-1);
    }

    public static ListNode merge(ListNode[] listNodes,int left,int right){
        if (left==right){
            return listNodes[left];
        }
        int mid = left + (right - left) / 2;
        ListNode leftNode = merge(listNodes, left, mid);
        ListNode rightNode = merge(listNodes, mid+1, right);
        return mergeTwo(leftNode,rightNode);
    }

    public static ListNode mergeTwo(ListNode left,ListNode right){
        ListNode l=left;
        ListNode r=right;

        ListNode ans=new ListNode();
        ListNode cur=ans;

        while (l!=null && r!=null){
            ListNode t=new ListNode();
            if (l.val<r.val){
                t.val=l.val;
                l=l.next;
            }else {
                t.val=r.val;
                r=r.next;
            }
            cur.next=t;
            cur=cur.next;
        }
        if (l!=null){
            cur.next=l;
        }
        if (r!=null){
            cur.next=r;
        }
        return ans.next;
    }
}
