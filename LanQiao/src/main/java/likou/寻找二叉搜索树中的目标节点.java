package likou;

import likou.entity.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：寻找二叉搜索树中的目标节点
 * @Date：2025/7/8 15:27
 * @Filename：寻找二叉搜索树中的目标节点
 */
public class 寻找二叉搜索树中的目标节点 {

    public static void main(String[] args) {

    }
    static List<Integer> list;
    public static int findTargetNode(TreeNode root, int cnt) {
        list=new ArrayList<>();
        dfs(root);
        return list.get(list.size()-cnt);
    }

    public static void dfs(TreeNode root){
        if (root==null){
            return;
        }
        dfs(root.left);
        list.add(root.val);
        dfs(root.right);
    }

}
