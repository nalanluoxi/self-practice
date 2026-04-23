package likou.力扣test2;


import java.util.*;

public class Test0415 {

    public static void main(String[] args) {


        System.out.println("\n=== DAG 逐层遍历 ===");
        //testDAGTraversal();
        DAGNode node1 = new DAGNode(1);
        DAGNode node2 = new DAGNode(2);
        DAGNode node3 = new DAGNode(3);
        DAGNode node4 = new DAGNode(4);
        DAGNode node5 = new DAGNode(5);
        DAGNode node6 = new DAGNode(6);

        node1.children.add(node2);
        node2.parents.add(node1);

        node1.children.add(node4);
        node4.parents.add(node1);

        node2.children.add(node3);
        node3.parents.add(node2);

        node4.children.add(node5);
        node5.parents.add(node4);

        node6.children.add(node5);
        node5.parents.add(node6);
        DAGNode[] list= new DAGNode[]{node1,node2,node3,node4,node5,node6};
        fun( list);
    }

    public static void fun(DAGNode[] list){
        Set<DAGNode> set=new HashSet<>();
        init(list,set);
        Set<DAGNode> visited=new HashSet<>();
        for (DAGNode dagNode : set) {
            dfs(dagNode,set,visited);
        }
    }

    public static void dfs(DAGNode node ,Set<DAGNode> set0,Set<DAGNode> visited){
        if (visited.contains(node)){
            return;
        }
        boolean flag=true;
        if (node.parents.size()==0){
           //System.out.println(node.val);
           //visited.add(node);
        }else {
            for (DAGNode parent : node.parents) {
                if (!visited.contains(parent)){
                    flag=false;
                    break;
                }

            }
        }
        if (flag){
            System.out.println(node.val);
            visited.add(node);
            for (DAGNode child : node.children) {
                dfs(child,set0,visited);
            }
        }
    }

    public static void init(DAGNode[] node,Set<DAGNode> set){
        for (DAGNode dagNode : node) {
            if (dagNode.parents.size()==0){
                set.add(dagNode);
            }
        }
    }


    static class DAGNode {
        public int val;
        public List<DAGNode> children;
        public List<DAGNode> parents;

        public DAGNode(int val) {
            this.val = val;
            this.children = new ArrayList<>();
            this.parents = new ArrayList<>();
        }
    }

    public static void traverseDAGByLevel(DAGNode[] nodes, int[][] edges) {
        Map<Integer, DAGNode> nodeMap = new HashMap<>();
        Map<Integer, Integer> inDegree = new HashMap<>();

        for (DAGNode node : nodes) {
            nodeMap.put(node.val, node);
            inDegree.putIfAbsent(node.val, 0);
        }

        for (int[] edge : edges) {
            int from = edge[0];
            int to = edge[1];
            nodeMap.get(from).children.add(nodeMap.get(to));
            inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
        }

        Queue<DAGNode> queue = new LinkedList<>();
        for (DAGNode node : nodes) {
            if (inDegree.get(node.val) == 0) {
                queue.offer(node);
            }
        }

        int level = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            System.out.print("第 " + level + " 层: ");

            for (int i = 0; i < size; i++) {
                DAGNode current = queue.poll();
                System.out.print(current.val + " ");

                for (DAGNode child : current.children) {
                    inDegree.put(child.val, inDegree.get(child.val) - 1);
                    if (inDegree.get(child.val) == 0) {
                        queue.offer(child);
                    }
                }
            }
            System.out.println();
            level++;
        }
    }

    public static void testDAGTraversal() {
        DAGNode node1 = new DAGNode(1);
        DAGNode node2 = new DAGNode(2);
        DAGNode node3 = new DAGNode(3);
        DAGNode node4 = new DAGNode(4);
        DAGNode node5 = new DAGNode(5);
        DAGNode node6 = new DAGNode(6);

        DAGNode[] nodes = {node1, node2, node3, node4, node5, node6};

        int[][] edges = {
                {1, 2},
                {1, 3},
                {2, 4},
                {3, 4},
                {3, 5},
                {4, 6},
                {5, 6}
        };

        traverseDAGByLevel(nodes, edges);
    }

}