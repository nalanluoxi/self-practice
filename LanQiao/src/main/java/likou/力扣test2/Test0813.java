package likou.力扣test2;

import likou.entity.ListNode;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：Test0813
 * @Date：2025/8/13 20:08
 * @Filename：Test0813
 */
public class Test0813 {

    public static void main(String[] args) {
        /*int[][]nums={
                {1,1,1},
                {1,0,1},
                {1,1,1}
        };
        setZeroes(nums);*/
/*        int[][] ints = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        System.out.println(ints[0][0]);
        System.out.println(ints[0][1]);
        System.out.println(ints[0][2]);*/
        /*List<Integer> list = spiralOrder(new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}

                0 0   - 0 2
                0 1   - 1 1
                2 0   - 0 0
        });*/
/*
        ListNode nod1=new ListNode(1);
        ListNode nod2=new ListNode(4);
        ListNode nod3=new ListNode(5);
        nod1.next=nod2;
        nod2.next=nod3;

        ListNode nod4=new ListNode(1);
        ListNode nod5=new ListNode(3);
        ListNode nod6=new ListNode(4);
        nod4.next=nod5;
        nod5.next=nod6;

        ListNode nod7=new ListNode(2);
        ListNode nod8=new ListNode(6);
        nod7.next=nod8;

        ListNode listNode = mergeKLists(new ListNode[]{nod1, nod4, nod7});
*/

   //     boolean b1 = canJump(new int[]{2, 3, 1, 1, 4});
        boolean b = canJump(new int[]{3,2,1,0,4});
        System.out.println(b);
    }
    public int coinChange(int[] coins, int amount) {
        int[]dp=new int[amount+1];
        dp[0] = 0;
        Arrays.sort(coins);
        for (int i = 1; i <= amount; i++) {
            dp[i]=Integer.MAX_VALUE;
            for (int coin : coins) {
                if (i>=coin&& dp[i - coin] != Integer.MAX_VALUE){
                    dp[i]=Math.min(dp[i],dp[i-coin]+1);
                }
            }
        }
        return dp[amount]==Integer.MAX_VALUE?-1:dp[amount];
    }

    public int rob(int[] nums) {
        if (nums.length==1){
            return nums[0];
        } else if (nums.length == 2) {
            return Math.max(nums[0],nums[1]);
        }
        int[]dp=new int[nums.length];
        dp[0]=nums[0];
        dp[1]=nums[1];
        for (int i = 2; i < nums.length; i++) {
            dp[i]=nums[i];
            if (i-3>=0){
                dp[i]+=Math.max(dp[i-2],dp[i-3]);
            }else {
                dp[i]+=dp[i-2];
            }
        }
        return Math.max(dp[nums.length-2],dp[nums.length-1]);
    }
    public int climbStairs(int n) {
        if (n<=2){
            return n;
        }
        int[]dp=new int[n];
        dp[0]=1;
        dp[1]=2;
        for (int i = 2; i < n; i++) {
            dp[i]=dp[i-1]+dp[i-2];
        }
        return dp[n-1];
    }
    public List<Integer> partitionLabels(String s) {
        int[]last=new int[26];
        int len = s.length();
        for (int i = 0; i < len; i++) {
            last[s.charAt(i)-'a']=i;
        }
        int start=0;
        int end=0;
        List<Integer> ans=new ArrayList<>();
        for (int i = 0; i < len; i++) {
            end=Math.max(end,last[s.charAt(i)-'a']);
            if (i==end){
                ans.add(end-start+1);
                start=end+1;
            }
        }
        return ans;
    }
    public int jump(int[] nums) {
        if (nums.length<=1){
            return 0;
        }
        int ans=0;
        int tmax=0;
        int nowmax=0;
        for (int i = 0; i < nums.length; i++) {
            tmax=Math.max(tmax,i+nums[i]);
            if (nowmax==i){
                ans++;
                nowmax=tmax;
                if (nowmax>=nums.length-1){
                    return ans;
                }
            }
        }

        return ans;
    }
    public static boolean canJump(int[] nums) {
        int tmax=nums[0];
        for (int i=0;i<=tmax;i++){
            if (i+nums[i]>=nums.length-1){
                return true;
            }
            int t = i + nums[i];
            tmax=Math.max(tmax,t);
        }
        return false;
    }

    public int maxProfit(int[] prices) {
        int ans=0;
        int max=prices[prices.length-1];
        for (int i = prices.length-1; i >=0; i--) {
            int t = max - prices[i];
            ans=Math.max(ans,t);
            max=Math.max(max,prices[i]);
        }
        return ans;
    }

    public static ListNode reverseKGroup(ListNode head, int k) {
        ListNode ans=new ListNode();
        ListNode cur=head;
        ListNode now=head;
        ListNode pre=ans;
        pre.next=head;
        int count=0;
        while (cur!=null){
            count++;
            cur=cur.next;
            if (count==k){
                ListNode reverse = helpreverse(now, k);
                ans.next=reverse;
                while (count!=0){
                    count--;
                    ans=ans.next;
                }
                now.next=cur;
                now=now.next;
            }
        }
        return pre.next;
    }

    public static ListNode helpreverse(ListNode listNode,int k){
        ListNode pre=null;
        ListNode cur=listNode;
        while (cur!=null && k>0){
            k--;
            ListNode next = cur.next;
            cur.next=pre;
            pre=cur;
            cur=next;
        }
        return pre;
    }

    public static ListNode sortList(ListNode head) {
        PriorityQueue<ListNode> queue=new PriorityQueue<>(new Comparator<ListNode>() {
            @Override
            public int compare(ListNode o1, ListNode o2) {
                return o1.val- o2.val;
            }
        });
        ListNode cur=head;
        while (cur!=null){
            queue.add(new ListNode(cur.val));
            cur=cur.next;
        }
        ListNode ans=new ListNode();
        cur=ans;
        while (!queue.isEmpty()){
            cur.next=queue.poll();
            cur=cur.next;
        }
        return ans.next;
    }
    public static ListNode mergeKLists(ListNode[] lists) {
        ListNode ans=new ListNode(Integer.MIN_VALUE);
        for (ListNode list : lists) {
            ans=addTwo(ans,list);
        }
        return ans.next;
    }
    private static ListNode addTwo(ListNode list1,ListNode list2){
        ListNode ans=new ListNode();
        ListNode cur=ans;
        while (list1!=null && list2!=null){
            if (list1.val<= list2.val){
                cur.next=new ListNode(list1.val);
                cur=cur.next;
                list1=list1.next;
            }else {
                cur.next=new ListNode(list2.val);
                cur=cur.next;
                list2=list2.next;
            }
        }
        if (list1!=null){
            cur.next=list1;
        }
        if (list2!=null){
            cur.next=list2;
        }
        return ans.next;
    }


    static class LRUCache {

        int capacity;
        Map<Integer,Node> map;
        Node head;
        Node tail;

        public LRUCache(int capacity) {
            map=new HashMap<>();
            head=new Node();
            tail=new Node();
            head.next=tail;
            tail.pre=head;
            this.capacity=capacity;
        }

        public int get(int key) {
            if (!map.containsKey(key)){
                return -1;
            }
            Node node = map.get(key);
            preNode(node);
            return node.value;
        }

        private void preNode(Node node){
            node.pre.next=node.next;
            node.next.pre=node.pre;

            node.next=head.next;
            head.next.pre=node;
            head.next=node;
            node.pre=head;
        }

        public void put(int key, int value) {
            if (map.containsKey(key)){
                Node node = map.get(key);
                node.value=value;
                preNode(node);
            }else {
                Node node = new Node(key, value);
                if (map.size()==capacity){
                    deleteLast();
                }
                map.put(key,node);

                Node tailpre = tail.pre;
                tailpre.next=node;
                node.pre=tailpre;
                tail.pre=node;
                node.next=tail;

                preNode(node);
            }
        }

        private void deleteLast() {
            Node dele = tail.pre;
            dele.pre.next=tail;
            tail.pre=dele.pre;
            map.remove(dele.key);
        }

        private class Node{
            int key;
            int value;
            Node pre;
            Node next;

            public Node() {
            }

            public Node(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }
    }


    public static void rotate(int[][] nums) {
        int n = nums.length;
        int[][]temp=new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                temp[j][n-i-1]=nums[i][j];
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                nums[i][j]=temp[i][j];
            }
        }
    }

    public static List<Integer> spiralOrder(int[][] nums) {
        List<Integer> ans=new ArrayList<>();
        int[][]dir={{0,1},{1,0},{0,-1},{-1,0}};
        int d=0;
        int x=0,y=0;
        int n = nums.length;
        int m = nums[0].length;
        int[][]visited=new int[n][m];
        while (ans.size()<n*m){
            ans.add(nums[x][y]);
            visited[x][y]=1;
            int nx = x + dir[d][0];
            int ny = y + dir[d][1];
            if (nx<0||nx>=n ||ny<0||ny>=m ||visited[nx][ny]!=0){
                d=(d+1)%4;
            }
            x=x+dir[d][0];
            y=y+dir[d][1];
        }
        return ans;
    }

    public static void setZeroes(int[][] nums) {

        int n = nums.length;
        int m = nums[0].length;
        int [][] visited=new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (nums[i][j]==0 && visited[i][j]==0){
                    help(nums,i,j,visited);
                }
            }
        }
    }

    public static void help(int[][]nums,int in,int jn ,int[][]visited){
        int n = nums.length;
        int m = nums[0].length;
        for (int i = 0; i < n; i++) {
            if (nums[i][jn]==0){
                continue;
            }
            nums[i][jn]=0;
            visited[i][jn]=1;
        }
        for (int i = 0; i < m; i++) {
            if (nums[in][i]==0){
                continue;
            }
            nums[in][i]=0;
            visited[in][i]=1;
        }
    }


}
