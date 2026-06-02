package likou.力扣test2;

import likou.entity.TreeNode;

import java.util.*;

/**
 * @author nalan_luoxi
 * @version 1.0
 * @email adrian0304@qq.com
 * @project self-practice
 * @package likou.力扣test2
 * @date 2026-05-27 15:51
 */
public class Test0527 {

    public static void main(String[] args) {
        String s = "3[a]2[bc]";
        System.out.println(decodeString(s));
    }





    public static String decodeString(String s) {
        LinkedList<String> deque = new LinkedList<>();
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                String nums = "" + c;
                i++;
                while (i < s.length() && Character.isDigit(s.charAt(i))) {
                    nums += s.charAt(i);
                    i++;
                }
                deque.add(nums);
            } else if (Character.isLetter(c) || c == '[') {
                deque.add(String.valueOf(c));
                i++;
            } else {
                i++;
                List<String> temp = new LinkedList<>();
                while (!deque.isEmpty() && !deque.peekLast().equals("[")) {
                    temp.add(deque.pollLast());
                }
                deque.pollLast();
                Collections.reverse(temp);
                String str = getString(temp);
                int nums = deque.isEmpty() ? 1 : Integer.valueOf(deque.pollLast());
                StringBuffer sb=new StringBuffer();
                while (nums>0){
                    sb.append(str);
                    nums--;
                }
                deque.add(sb.toString());
            }
        }
        String ans = getString(deque);
        return ans;
    }

    public static String getString(List<String> temp) {
        String str = "";
        for (String s : temp) {
            str += s;
        }
        return str;
    }

    static int ans;

    public static int sumNumbers(TreeNode root) {
        if (root == null) {
            return 0;
        }
        ans = 0;
        Deque<TreeNode> deque = new LinkedList<>();
        sum(root, deque);
        return ans;
    }

    public static void sum(TreeNode root, Deque<TreeNode> deque) {
        deque.add(root);
        if (root.left == null && root.right == null) {
            add(deque);
        }
        if (root.left != null) {
            sum(root.left, deque);
        }
        if (root.right != null) {
            sum(root.right, deque);
        }
        deque.removeLast();
    }

    public static void add(Deque<TreeNode> deque) {
        int temp = 0;
        for (TreeNode treeNode : deque) {
            temp = temp * 10 + treeNode.val;
        }
        ans += temp;
    }




    public static TreeNode buildTree(int[] preorder, int[] inorder) {
        if (preorder.length == 0 || inorder.length == 0) {
            return null;
        }
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i], i);
        }
        return buildTree(preorder, 0, 0, inorder.length - 1, map);
    }

    public static TreeNode buildTree(int[] preorder, int rootindex, int left, int right, Map<Integer, Integer> map) {
        if (left > right) {
            return null;
        }
        TreeNode root = new TreeNode();
        root.val = preorder[rootindex];
        Integer i = map.get(preorder[rootindex]);
        root.left = buildTree(preorder, rootindex + 1, left, i - 1, map);
        root.right = buildTree(preorder, rootindex + (i - left) + 1, i + 1, right, map);
        return root;
    }

}
