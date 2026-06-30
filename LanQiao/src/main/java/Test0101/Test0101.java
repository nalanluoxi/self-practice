package Test0101;

public class Test0101 {

    public static void main(String[] args) {

        Node n1=new Node(1);
        Node n2=new Node(2);
        Node n3=new Node(3);
        Node n4=new Node(4);
        Node n5=new Node(5);

        n1.next=n2;
        n2.next=n3;
        n3.next=n4;
        n4.next=n5;
        Node test = test(n1);
        while (test!=null){
            System.out.println(test.val);
            test=test.next;
        }
    }


    public static Node test(Node head){
        Node pre=null;
        Node cur=head;
        while (cur!=null){
            Node next = cur.next;
            cur.next=pre;
            pre=cur;
            cur=next;
        }
        return pre;
    }



    static class Node{
        int val;
        Node next;

        public Node(int val) {
            this.val = val;
        }
    }

}
