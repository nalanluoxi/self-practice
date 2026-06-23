package likou.力扣test2;

import com.sun.source.tree.Tree;
import likou.entity.ListNode;
import likou.entity.TreeNode;

import java.util.*;

/**
 * @author nalan_luoxi
 * @version 1.0
 * @email adrian0304@qq.com
 * @project self-practice
 * @package likou.力扣test2
 * @date 2026-06-23 14:40
 */
public class Test0623 {
    public static void main(String[] args) {
        int[] nums = {10, 9, 2, 5, 3, 7, 101, 18};
        System.out.println(lengthOfLIS(nums));
    }

    public static ListNode mergeKLists(ListNode[] lists) {
        if (lists.length==0){
            return null;
        } else if (lists.length==1) {
            return lists[0];
        }
        ListNode cur=lists[0];
        for (int i = 1; i < lists.length; i++) {
            cur=mergeTwo(cur,lists[i]);
        }
        return cur;
    }

    public static ListNode mergeTwo(ListNode n1, ListNode n2) {
        ListNode ans = new ListNode();
        ListNode cur = ans;
        while (n1 != null && n2 != null) {
            ListNode listNode = new ListNode();
            if (n1.val < n2.val) {
                listNode.val = n1.val;
                n1=n1.next;
            } else {
                listNode.val = n2.val;
                n2=n2.next;
            }
            cur.next = listNode;
            cur = cur.next;
        }
        if (n1 != null) {
            cur.next=n1;
        }
        if (n2!=null){
            cur.next=n2;
        }

        return ans.next;
    }

    public static TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {
        Map<TreeNode, TreeNode> map = new HashMap<>();
        if (root == null) {
            return null;
        }
        init(map, root);
        // Deque<TreeNode>

        return null;
    }

    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        Map<TreeNode, TreeNode> map = new HashMap<>();
        if (root == null) {
            return null;
        }
        map.put(root, null);
        init(map, root);
        List<TreeNode> path = new ArrayList<>();
        TreeNode cur = p;
        while (cur != null) {
            path.add(cur);
            cur = map.get(cur);
        }
        cur = q;
        while (cur != null) {
            if (path.contains(cur)) {
                return cur;
            } else {
                cur = map.get(cur);
            }
        }

        return root;
    }


    public static void init(Map<TreeNode, TreeNode> map, TreeNode root) {
        if (root == null) {
            return;
        }
        if (root.left != null) {
            map.put(root.left, root);
            init(map, root.left);
        }
        if (root.right != null) {
            map.put(root.right, root);
            init(map, root.right);
        }
    }


    public static int lengthOfLIS(int[] nums) {
        int len = nums.length;
        if (len <= 1) {
            return nums.length;
        }
        int[] dp = new int[len];

        int ans = 1;
        for (int i = len - 1; i >= 0; i--) {
            dp[i] = 1;
            for (int j = i + 1; j < len; j++) {
                // System.out.println(dp[i]+" "+dp[j]);
                if (nums[i] < nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            ans = Math.max(dp[i], ans);
        }
        return ans;
    }
}
