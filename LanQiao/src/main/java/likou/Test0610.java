package likou;

import likou.entity.TreeNode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nalan_luoxi
 * @version 1.0
 * @email adrian0304@qq.com
 * @project self-practice
 * @package likou
 * @date 2026-06-10 14:59
 */
public class Test0610 {
    public static void main(String[] args) {

    }


    public static TreeNode buildTree(int[] inorder, int[] postorder) {


        Map<Integer,Integer>map=new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i],i);
        }

        return null;
    }

    public static TreeNode dfs1(int[] postorder ,int index,int left,int right){

        return null;
    }
}
