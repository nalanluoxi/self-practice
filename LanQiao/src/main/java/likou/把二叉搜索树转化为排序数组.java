package likou;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：把二叉搜索树转化为排序数组
 * @Date：2025/6/14 10:20
 * @Filename：把二叉搜索树转化为排序数组
 */
public class 把二叉搜索树转化为排序数组 {


    public static void main(String[] args) {
        Node node = new Node(4);
        Node node1 = new Node(2);
        Node node2 = new Node(5);
        Node node3 = new Node(1);
        Node node4 = new Node(3);
        node.left = node1;
        node.right = node2;
        node1.left = node3;
        node1.right = node4;
        Node node5 = treeToDoublyList(node);
        while (node5 != null) {
            System.out.println(node5.val);
            node5 = node5.right;
        }
    }
    public static Node treeToDoublyList(Node root) {
        if (root == null) {
            return null;
        }
        list = new ArrayList<>();
        dfs(root);
        Node head = new Node();
        Node cur = head;
        for (Node n : list) {
            Node t = new Node(n.val);
            cur.right = t;
            t.left = cur;
            cur = cur.right;
        }
        return head.right;
    }

    static List<Node> list;

    public static void dfs(Node root) {
        if (root == null) {
            return;
        }
        dfs(root.left);
        list.add(root);
        dfs(root.right);
    }


    // Definition for a Node.
    static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node() {
        }

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, Node _left, Node _right) {
            val = _val;
            left = _left;
            right = _right;
        }
    }

    ;

}
