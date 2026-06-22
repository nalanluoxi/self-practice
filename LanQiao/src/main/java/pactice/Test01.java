package pactice;

import lombok.Data;

public class Test01 {
    // 112345  12345

    public static void main(String[] args) {
        Node n1=new Node(1);
        Node n2=new Node(1);
        Node n3=new Node(2);
        Node n4=new Node(3);
        Node n5=new Node(4);
        Node n6=new Node(5);

        n1.next=n2;
        n2.next=n3;
        n3.next=n4;
        n4.next=n5;
        n5.next=n6;

       Node res= test(n1);
        while(res!=null){
            System.out.println(res.val);
            res=res.next;
        }
    }

    public static Node test(Node head){
        if (head==null|| head.next==null){
            return head;
        }
        Node cur=head;
        Node fast=head.next;
        while (fast!=null){
            if(fast.val==cur.val){
                fast=fast.next;
            }else{
                cur.next=fast;
                cur=cur.next;
                fast=fast.next;
            }
        }
        return head;
    }


    @Data
    static class Node {
        int val;
        Node next;

        public Node(int val) {
            this.val = val;
        }
    }

}
