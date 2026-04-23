package likou.LRU算法;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 纳兰洛熙
 * @Package：likou.LRU算法
 * @Project：LanQiaoBei
 * @name：MyLRU
 * @Date：2025/5/12 16:56
 * @Filename：MyLRU
 */
public class MyLRU {
    private int capacity;
    private Node head;
    private Node tail;
    private int n;

    private Map<Integer,Node> map;
    public MyLRU(int capacity) {
        this.capacity=capacity;
        this.n=0;
        this.map=new HashMap<>();
        this.head=new Node(0,0);
        this.tail=new Node(0,0);
        head.next=tail;
        tail.pre=head;
    }

    public void put(int key, int value) {
        Node node=null;
        if (map.containsKey(key)){
            node= map.get(key);
            node.value=value;
        }else {
            if (n==capacity){
                Node deleteNode = tail.pre;
                deleteNode(deleteNode);
                map.remove(deleteNode.key);
            }
            node=new Node(key,value);
        }
        map.put(key,node);
        replaceNode(node);
    }

    public int get(int key) {
        if (map.containsKey(key)){
            Node node= map.get(key);
            replaceNode(node);
            return node.value;
        }
        return -1;
    }

    private void replaceNode(Node node){
        deleteNode(node);
        node.next=head.next;
        node.pre=head;
        head.next.pre=node;
        head.next=node;
    }

    private void deleteNode(Node node){
       if (node.next!=null){
           node.pre.next=node.next;
           node.next.pre=node.pre;
       }
    }
    private class Node{
        int key;
        int value;
        Node pre;
        Node next;
        public Node(int key,int value){
            this.key=key;
            this.value=value;
        }
    }
}
