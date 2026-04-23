package likou;

public class Test0414 {

    public static void main(String[] args) {


        Node n1 = new Node();
        Node n2 = new Node();
        Node n3 = new Node();
        Node n4 = new Node();
        Node n5 = new Node();
        Node n6 = new Node();
        Node n7 = new Node();
        n1.val = 1;
        n1.next = n2;

        n2.val = 2;
        n2.next = n3;

        n3.val = 3;
        n3.next = n4;

        n4.val = 3;
        n4.next = n5;

        n5.val = 4;
        n5.next = n6;

        n6.val = 4;
        n6.next = n7;

        n7.val = 5;

        Node ans = help(n1);
        while (ans != null) {
            System.out.println(ans.val);
            ans = ans.next;
        }

    }

    public static Node help(Node node) {
        if (node == null || node.next == null) {
            return node;
        }

        Node n1 = new Node();
        Node ans= n1;
        Node n2 = node;
        while (n2 != null) {
            if (n2.next!=null && n2.val==n2.next.val){
                int t = n2.val;
                while (n2.next != null && n2.val == t) {
                    n2 = n2.next;
                }
            }else {
                n1.next=n2;
                n1=n1.next;
                n2=n2.next;
            }

        }

        return ans.next;
    }

    static class Node {
        public int val;
        public Node next;
    }

}
