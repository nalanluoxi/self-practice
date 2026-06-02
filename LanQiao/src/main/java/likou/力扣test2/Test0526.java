package likou.力扣test2;

import likou.entity.TreeNode;

import java.util.*;

/**
 * @author nalan_luoxi
 * @version 1.0
 * @email adrian0304@qq.com
 * @project self-practice
 * @package likou
 * @date 2026-05-26 23:24
 */
public class Test0526 {


    public static void main(String[] args) {
        char[][]temp={
                {'1','1','1','1','0'},
                {'1','1','0','1','0'},
                {'1','1','0','0','0'},
                {'0','0','0','0','0'}
        };
        System.out.println(numIslands(temp));
    }


    //in 左中右
    //post 左右中
    public static TreeNode buildTree(int[] inorder, int[] postorder) {
        if (inorder.length==0||postorder.length==0){
            return null;
        }
        Map<Integer,Integer> map=new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i],i);
        }
        return dfs(inorder,postorder,postorder.length-1,0,inorder.length-1,map);
    }

    //in 左中右
    //post 左右中  根左右中                                                    当前处理中序in范围
    public static TreeNode dfs(int[]inoder,int[]posorder,int rootindex,int left ,int right,Map<Integer,Integer> map){
        if (left>right){
            return null;
        }
        TreeNode root = new TreeNode();
        root.val=posorder[rootindex];
        Integer i = map.get(posorder[rootindex]);
        root.left=dfs(inoder,posorder,rootindex-right+i-1,left,i-1,map);
        root.right=dfs(inoder,posorder,rootindex-1,i+1,right,map);
        return root;
    }

    //

    public static List<Integer> rightSideView(TreeNode root) {
        if (root==null){
            return new ArrayList<>();
        }
        List<Integer>result=new ArrayList<>();
        Deque<TreeNode> deque=new LinkedList<>();

        deque.add(root);
        while (!deque.isEmpty()){
            int size = deque.size();
            List<TreeNode> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode treeNode = deque.pollFirst();
                list.add(treeNode);
                if (treeNode.left!=null){
                    deque.add(treeNode.left);
                }
                if (treeNode.right!=null){
                    deque.add(treeNode.right);
                }
            }
            result.add(list.get(list.size()-1).val);
        }
        return result;
    }



    static int max;
    public static int maxPathSum(TreeNode root) {
        if (root==null){
            return 0;
        }
        max=root.val;
        dfs2(root);
        return max;
    }

    public static int dfs2(TreeNode root){
        if (root==null){
            return 0;
        }
        int left=Math.max(dfs2(root.left),0);
        int right=Math.max(dfs2(root.right),0);
        max=Math.max(max,left+right+root.val);
        return Math.max(left, right)+root.val;
    }


    public static int numIslands(char[][] grid) {

        int x = grid.length;
        int y = grid[0].length;

        int[][] visited = new int[x][y];
        int count = 0;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (grid[i][j] == '1' && visited[i][j] == 0) {
                    count++;
                    //visited[i][j] = 1;
                    dfs(grid, visited, i, j);
                }
            }
        }
        return count;

    }

    public static void dfs(char[][] grid, int[][] visited, int i, int j) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length) {
            return;
        }
        if (grid[i][j] == '1' && visited[i][j] == 0) {
            visited[i][j] = 1;
            dfs(grid, visited, i - 1, j);
            dfs(grid, visited, i + 1, j);
            dfs(grid, visited, i, j - 1);
            dfs(grid, visited, i, j + 1);
        }
    }
}
