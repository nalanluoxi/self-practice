package likou.力扣test2.LRU;

import likou.力扣test2.解码方法2;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2.LRU
 * @Project：LanQiaoBei
 * @name：MyLRU
 * @Date：2025/7/1 22:57
 * @Filename：MyLRU
 */
public class MyLRU {
    //最近最少使用
    private  int capacity;
    private int size;
    private Node head;
    private Node tail;

    private Map<Integer,Node> map;

    public MyLRU(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.head = new Node(-1,-1);
        this.tail = new Node(-1,-1);
        head.next = tail;
        tail.pre = head;
        map=new HashMap<>();
    }

    public int getSize(){
        return size;
    }

    public void put(int key,int value){
        if (map.containsKey(key)){
            Node node = map.get(key);
            node.value = value;
            update(node);
        }else {
            if (size==capacity){
               removelast();
            }
            size++;
            Node node = new Node(key,value);
            map.put(key,node);
            head.next.pre=node;
            node.next=head.next;
            node.pre=head;
            head.next=node;
        }
    }

    public void removelast(){
        Node remove = tail.pre;
        map.remove(remove.key);
        remove.pre.next=tail;
        tail.pre=remove.pre;
        size--;
    }
    public int get(int key){
        if (!map.containsKey(key)){
            return -1;
        }
        Node node = map.get(key);
        update(node);
        return node.value;
    }

    public void update(Node node){
        //移除
        node.pre.next=node.next;
        node.next.pre=node.pre;


        //前置
        head.next.pre=node;
        node.next=head.next;
        node.pre=head;
        head.next=node;
    }

    @Data
    private class Node{
        private Node pre;
        private Node next;
        private int key;
        private int value;

        public Node( int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
}
