public class MyLinkedList {
    int size;
    ListNode head;


    public MyLinkedList() {
        size=0;
        head=new ListNode();
    }

    public int get(int index) {
        if (index<0||index>size){
            return -1;
        }
        ListNode order=head;
        for (int i = 0; i <= index; i++) {
            order=order.next;
        }
        return order.val;
    }

    public void addAtHead(int val) {
        addAtIndex(0,val);
    }

    public void addAtTail(int val) {
        addAtIndex(size,val);
    }

    public void addAtIndex(int index, int val) {
        if (index>size){
            return;
        }
        if (index<0){
            index=0;
        }

        size++;

        ListNode befor=head;
        for (int i = 0; i <= index; i++) {
            befor=befor.next;
        }

        ListNode newlink=new ListNode(val);
        newlink.next=befor.next;
        befor.next=newlink;

    }

    public void deleteAtIndex(int index) {
        if (index>size||index<0){
            return;
        }
        size--;
        if (index==0){
            head=head.next;
            return;
        }

        ListNode order=head;
        for (int i = 0; i < index; i++) {
            order=order.next;
        }

        order.next=order.next.next;



    }


}
