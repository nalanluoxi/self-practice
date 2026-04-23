/**
 * @Author 纳兰洛熙
 * @Package：PACKAGE_NAME
 * @Project：LanQiaoBei
 * @name：Test0820
 * @Date：2025/8/20 16:20
 * @Filename：Test0820
 */
public class Test0820 {


    public static void main(String[] args) {
        int[]arr={4,8,7,5,1,6,3,2};
        int[]brr={8,4,5,7,6,2,3,1};
        TreeNode treeNode = buildTree(arr, brr);
    }
    static int [] inorder;
    static int[] postorder;
    public static TreeNode buildTree(int[] inorders, int[] postorders) {
        // write your code here.
        inorder=inorders;
        postorder=postorders;
        return help(0, inorder.length-1, 0,postorders.length-1);
    }

    public static TreeNode help(int inl,int inr,int pl,int lr){

        if (inl >inr){
            return null;
        }
        if (inl ==inr){
            return new TreeNode(inorder[inl]);
        }
        int rootIndex = lr;
        int rootval = postorder[rootIndex];
        int left=getIndex(rootval,inorder);
        TreeNode root=new TreeNode(rootval);
        int index=0;
        if (left>0) {
            index = getIndex(inorder[left - 1], postorder);
        }else {
            index=-1;
        }
        root.left=help(inl,left-1,pl,pl+(left-1-inl));

        root.right=help(left+1,inr,index+1,lr-1 );

        return root;
    }

    public static int getIndex(int val,int[] nums){
        for (int i = 0; i < nums.length; i++) {
            if (nums[i]==val){
                return i;
            }
        }
        return -1;
    }

    static class TreeNode {
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
}
