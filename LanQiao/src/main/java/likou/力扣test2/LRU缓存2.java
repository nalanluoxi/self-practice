package likou.力扣test2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：LRU缓存2
 * @Date：2025/6/6 8:25
 * @Filename：LRU缓存2
 */
public class LRU缓存2 {

    public static void main(String[] args) {
        LRUCache lruCache = new LRUCache(2);
        lruCache.put(1, 1);
        lruCache.put(2, 2);
        int temp = lruCache.get(1);
        System.out.println("查询1：" + temp);
        lruCache.put(3, 3);
        temp = lruCache.get(2);
        System.out.println("查询2:" + temp);
        lruCache.put(4, 4);
        temp = lruCache.get(1);
        System.out.println("查询1：" + temp);
        int i3 = lruCache.get(3);
        System.out.println("查询2：" + i3);
        int i4 = lruCache.get(4);
        System.out.println("查询4：" + i4);

    }


    static class LRUCache {
        int capacity;
        Map<Integer, Node> map;
        Node head;
        Node tail;

        public LRUCache(int capacity) {
            this.capacity = capacity;
            map = new HashMap<>();
            head = new Node(-1, -1);
            tail = new Node(-1, -1);
            head.next = tail;
            tail.pre=head;
        }

        public int get(int key) {
            if (!map.containsKey(key)){
                return -1;
            }
            Node node = map.get(key);
            update(node);
            return node.value;
        }

        private void update(Node node){
            node.pre.next=node.next;
            node.next.pre=node.pre;

            head.next.pre=node;
            node.next=head.next;
            node.pre=head;
            head.next=node;
            return;
        }

        public void put(int key, int value) {
            if (map.containsKey(key)){
                Node node = map.get(key);
                node.value=value;
                update(node);
                return;
            }else {
                if (capacity==map.size()){
                    removeLast();
                }
                Node node = new Node(key, value);
                map.put(key,node);
                tail.pre.next=node;
                node.pre=tail.pre;
                tail.pre=node;
                node.next=tail;
                update(node);
            }
        }

        private void removeLast() {
            Node pre = tail.pre;
            map.remove(pre.key);
            pre.pre.next=tail;
            tail.pre=pre.pre;
            return;
        }

        private static class Node {
            int key;
            int value;
            Node pre;
            Node next;
            public Node(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }
    }

    public static class LFUCache {

        int capacity;
        Map<Integer, Node> keyToNode;
        Map<Integer, LinkedList<Node>> freqToNode;

        private static class Node {
            int key;
            int value;
            int freq;

            public Node(int key, int value) {
                this.key = key;
                this.value = value;
                this.freq = 1;
            }
        }

        public void LRUCache(int capacity) {
            this.capacity = capacity;
            keyToNode = new HashMap<>();
            freqToNode = new HashMap<>();
        }

        public int get(int key) {
            if (!keyToNode.containsKey(key)) {
                return -1;
            }
            Node node = keyToNode.get(key);
            update(node);
            return node.value;
        }

        private void update(Node node) {
            int freq = node.freq;
            freqToNode.get(freq).remove(node);
            if (freqToNode.get(freq).isEmpty()) {
                freqToNode.remove(freq);
            }
            int tempmin = 1;
            while (!freqToNode.isEmpty() && freqToNode.get(tempmin).isEmpty()) {
                freqToNode.remove(tempmin++);
            }

            node.freq++;
            if (!freqToNode.containsKey(freq + 1)) {
                freqToNode.put(freq + 1, new LinkedList<>());
            }
            freqToNode.get(freq + 1).addFirst(node);
        }

        public void put(int key, int value) {
            if (keyToNode.containsKey(key)) {
                Node node = keyToNode.get(key);
                node.value = value;
                update(node);
                node.freq++;
                keyToNode.put(key, node);
            } else {
                if (keyToNode.size() == capacity) {
                    removeLast();
                }
                Node node = new Node(key, value);
                keyToNode.put(key, node);
                if (!freqToNode.containsKey(1)) {
                    freqToNode.put(1, new LinkedList<>());
                }
                freqToNode.get(1).addFirst(node);
            }
        }

        private void removeLast() {
            int min = 1;
            while (!freqToNode.isEmpty() && !freqToNode.containsKey(min)) {
                min++;
            }
            Node node = freqToNode.get(min).removeLast();
            //Node node = nodes.removeLast();
            keyToNode.remove(node.key);

            int tempmin = 1;
            while (!freqToNode.isEmpty() && freqToNode.get(tempmin).isEmpty()) {
                freqToNode.remove(tempmin++);
            }

        }


    }

}
