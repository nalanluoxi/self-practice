package likou.力扣test2;

import likou.entity.TreeNode;

import java.util.*;

/**
 * @author nalan_luoxi
 * @version 1.0
 * @email adrian0304@qq.com
 * @project self-practice
 * @package likou.力扣test2
 * @date 2026-06-14 21:19
 */
public class Test0614 {


    public static void main(String[] args) {

        int[]arr=new int[]{9,3,15,20,7};
        int[]brr=new int[]{9,15,7,20,3};

        TreeNode treeNode = buildTree(arr, brr);
        System.out.println(treeNode.val);
    }



    public static int[] findOrder(int n, int[][] nums) {
        List<List<Integer>> list=new ArrayList<>();
        int[] indegree=new int[n];
        for (int i = 0; i < n; i++) {
            list.add(new ArrayList<>());
        }
        for (int[] temp : nums) {
            list.get(temp[1]).add(temp[0]);
            indegree[temp[0]]++;
        }
        Queue<Integer>queue=new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (indegree[i]==0){
                queue.add(i);
            }
        }
        int[]res=new int[n];
        int index=0;
        while (!queue.isEmpty()){
            Integer poll = queue.poll();
            res[index++]=poll;
            for (Integer integer : list.get(poll)) {
                indegree[integer]--;
                if (indegree[integer]==0){
                    queue.offer(integer);
                }
            }
        }
        return index==n?res:new int[0];
    }

    public static int[] findOrder2(int numCourses, int[][] prerequisites) {
        List<List<Integer>> list=new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            list.add(new ArrayList<>());
        }
        int[] indegree=new int[numCourses];
        for (int[] temp : prerequisites) {
            list.get(temp[1]).add(temp[0]);
            indegree[temp[0]]++;
        }
        Queue<Integer> queue=new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) queue.offer(i);
        }
        int[] order = new int[numCourses];
        int index=0;
        while (!queue.isEmpty()){
            Integer poll = queue.poll();
            order[index++]=poll;
            for (Integer integer : list.get(poll)) {
                if (--indegree[integer]==0){
                    queue.offer(integer);
                }
            }
        }
        return index==numCourses?order:new int[0];


    }
















    public static TreeNode buildTree(int[] inorder, int[] postorder) {
        Map<Integer,Integer> map=new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i],i);
        }
        return dfs1(map,postorder,postorder.length-1,0,postorder.length-1);
    }

    //左 中 右。  左右中
    public static TreeNode dfs1(Map<Integer,Integer> map,int[]post,int cur,int left,int right){
        if (left>right || cur<0){
            return null;
        }
        TreeNode treeNode = new TreeNode();
        treeNode.val=post[cur];
        Integer i = map.get(treeNode.val);
        treeNode.right=dfs1(map,post,cur-1,i+1,right);
        treeNode.left=dfs1(map,post,cur-right+i-1,left,i-1);
        return treeNode;
    }
}
