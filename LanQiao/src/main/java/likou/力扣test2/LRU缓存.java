package likou.力扣test2;

import ch.qos.logback.core.joran.action.NewRuleAction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：LRU缓存
 * @Date：2025/5/20 23:21
 * @Filename：LRU缓存
 */
public class LRU缓存 {
    public static void main(String[] args) {
        LRUCache lruCache = new LRUCache(2);
        lruCache.put(1,1);
        lruCache.put(2,2);
         int temp=lruCache.get(1);
        System.out.println("查询1："+temp);
        lruCache.put(3,3);
        temp = lruCache.get(2);
        System.out.println("查询2:"+temp);
        lruCache.put(4,4);
        temp = lruCache.get(1);
        System.out.println("查询1："+temp);
        int i3 = lruCache.get(3);
        System.out.println("查询2："+i3);
        int i4 = lruCache.get(4);
        System.out.println("查询4："+i4);
    }
    static  class LRUCache {
        private int capacity;
        private  Node head;
        private Node tail;
        private Map<Integer,Node>map;

        public LRUCache(int capacity) {
            this.capacity=capacity;
            head=new Node(-1,-1);
            tail=new Node(-1,-1);
            head.next=tail;
            tail.pre=head;
            map=new HashMap<>();
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
            delete(node);
            /*node.pre.next=node.next;
            node.next.pre=node.pre;

            node.next=head.next;
            node.pre=head;
            head.next=node;*/
            head.next.pre=node;
            node.next=head.next;
            node.pre=head;
            head.next=node;
        }

        private void delete(Node node){
            if (node.next!=null){
                node.pre.next=node.next;
                node.next.pre=node.pre;
            }
        }

        public void put(int key, int value) {
            Node node=null;
            if (map.containsKey(key)){
                node = map.get(key);
                node.value=value;

            }else {
                if (map.size()==capacity){
                    Node tail = this.tail.pre;
                    delete(tail);
                    map.remove(tail.key);
                }
                node = new Node(key, value);
             /*
                node.next=head.next;
                head.next.pre=node;

                node.pre=head;
                head.next=node;*/
               /* if (map.size()>capacity){
                    map.remove(tail.pre.key);
                    tail.pre=tail.pre.pre;
                    tail.pre.next=tail;
                }*/
            }
            map.put(key,node);
            preNode(node);
        }

        private class Node{
            int key;
            int value;
            Node pre;
            Node next;

            public Node(int key, int value) {
                this.key = key;
                this.value = value;
            }

            public Node() {
            }
        }
    }
}
