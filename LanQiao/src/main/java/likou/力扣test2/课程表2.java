package likou.力扣test2;

import likou.entity.ListNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：课程表2
 * @Date：2025/7/15 12:05
 * @Filename：课程表2
 */
public class 课程表2 {
    public static void main(String[] args) {
/*        int[][] num = {
                {1, 2, 3},
                {4, 5, 6}
        };
        int[] element = findElement(num, 2, 3, 6);
        for (int i : element) {
            System.out.println(i);
        }*/
        ListNode n1=new ListNode(1);
        ListNode n2=new ListNode(2);
        ListNode n3=new ListNode(3);
        n1.next=n2;
        n2.next=n3;
        ListNode listNode = ReverseList(n1);
        while (listNode!=null){
            System.out.println(listNode.val);
            listNode=listNode.next;
        }
    }

    public static ListNode ReverseList (ListNode head) {
        // write code here
        ListNode cur=head;
        ListNode pre=null;
        while (cur!=null){
            ListNode next = cur.next;
            cur.next=pre;
            pre=cur;
            cur=next;
        }
        return pre;
    }
    public static int[] findElement(int[][] mat, int n, int m, int x) {
        // write code here
        int i = mat.length-1;
        int j = 0;
        while (i >= 0 && j < mat[0].length) {
            if (mat[i][j] == x) {
                return new int[]{i, j};
            } else if (mat[i][j] > x) {
                i--;
            } else {
                j++;
            }
        }
        return new int[0];
    }

    static List<List<Integer>> list;
    static boolean ans;
    static int[] visited;
    static int index;

    static int[] anslist;


    public int[] findOrder(int numCourses, int[][] prerequisites) {
        list = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            list.add(new ArrayList<>());
        }
        visited = new int[numCourses];
        anslist = new int[numCourses];
        index = numCourses - 1;
        ans = true;
        for (int[] num : prerequisites) {
            list.get(num[1]).add(num[0]);
        }
        for (int i = 0; i < numCourses && ans; i++) {
            if (visited[i] == 0) {
                dfs(i);
            }
        }
        if (!ans) {
            return new int[0];
        }
        return anslist;
    }

    public static void dfs(int n) {
        visited[n] = 1;
        for (Integer i : list.get(n)) {
            if (visited[i] == 0) {
                dfs(i);
                if (!ans) {
                    return;
                }
            } else if (visited[i] == 1) {
                ans = false;
                return;
            }
        }
        visited[n] = 2;
        anslist[index--] = n;
    }
}
