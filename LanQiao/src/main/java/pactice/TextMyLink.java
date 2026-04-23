package pactice;

import java.util.ArrayList;
import java.util.List;

public class TextMyLink {
   /* public static void main(String[] args) {

    }
    public static ListNode reverseList(ListNode head) {
        ListNode before = null;
        ListNode now=head;
        ListNode temp=null;
        while (now!=null){
            temp=now.next;
            now.next=before;
            before=now;
            now=temp;
        }
        return now;
    }

    public ListNode detectCycle(ListNode head) {
        ListNode fast=head;
        ListNode slow=head;
        while (fast!=null&&fast.next!=null){
            fast=fast.next.next;
            slow=slow.next;
            if (slow==fast){
                slow=head;
                while (slow!=fast){
                    slow=slow.next;
                    fast=fast.next;
                }
                return slow;
            }
        }
        return null;
    }*/

    static List<List<Integer>> resultlist=new ArrayList<>();
    static List<Integer> answer=new ArrayList<>();
    public List<List<Integer>> combinationSum3(int k, int n) {
        backing(k,n,0,1);
        return resultlist;
    }

    public static void backing(int k,int n,int sum,int startindex){
        if (sum>n){
            return;
        }

        if (answer.size()==k){
            if (sum==n){
                resultlist.add(answer);
                return;
            }
        }

        for (int i = startindex; i < 9-(k-answer.size())+1; i++) {
            sum+=i;
            answer.add(i);
            backing(k,n,sum,i+1);
            sum-=i;
            answer.remove(answer.size()-1);
        }
    }


}
