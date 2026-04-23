package likou.力扣test2;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：把二叉搜索树转化为排序的双向链表
 * @Date：2025/7/3 22:32
 * @Filename：把二叉搜索树转化为排序的双向链表
 */
public class 把二叉搜索树转化为排序的双向链表 {
    public static void main(String[] args) {

    }

    static List<Node> list;
    public static Node treeToDoublyList(Node root) {
        if (root==null){
            return null;
        }
        list=new ArrayList<>();
        dfs(root);
        Node head = new Node();
        Node now=head;
        for (Node node : list) {
            now.right=node;
            node.left=now;
            now=now.right;
        }
        now.right=head.right;
        head.right.left=now;
        return head.right;
    }

    public static void dfs(Node root){
        if (root==null){
            return;
        }
        dfs(root.left);
        list.add(root);
        dfs(root.right);
    }
    static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node() {}

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val,Node _left,Node _right) {
            val = _val;
            left = _left;
            right = _right;
        }
    };
}
