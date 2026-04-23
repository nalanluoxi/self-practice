package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：路径总和
 * @Date：2025/5/12 15:17
 * @Filename：路径总和
 */
public class 路径总和 {
    public static void main(String[] args) {
      /*  TreeNode root=new TreeNode(1);
        root.left=new TreeNode(2);
        root.right=new TreeNode(3);*/
        TreeNode root=new TreeNode(5);
        root.left=new TreeNode(4);
        root.right=new TreeNode(8);
        root.left.left=new TreeNode(11);
        root.left.left.left=new TreeNode(7);
        root.left.left.right=new TreeNode(2);
        root.right.left=new TreeNode(13);
        root.right.right=new TreeNode(4);
        root.right.right.right=new TreeNode(1);
        System.out.println(hasPathSum(root,22));

    }




    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public static boolean hasPathSum(TreeNode root, int targetSum) {
        if(root==null){
            return false;
        }
        return dfs(root,targetSum,root.val);
    }

    public static boolean dfs(TreeNode root,int targetSum,int sum){
        if(root.left==null && root.right==null){
            if (targetSum==sum){
                return true;
            }
        }
        boolean left=false;
        boolean right=false;
        if (root.left!=null){
            left = dfs(root.left, targetSum, sum + root.left.val);
        }
        if (root.right!=null){
            right = dfs(root.right, targetSum, sum + root.right.val);
        }
        return left||right;
    }

}
