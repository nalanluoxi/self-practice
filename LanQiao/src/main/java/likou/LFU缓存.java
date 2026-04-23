package likou;

import likou.LFU算法.MyLFU;

import java.util.HashMap;
import java.util.LinkedHashSet;

import java.util.Map;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：LFU缓存
 * @Date：2025/6/4 9:22
 * @Filename：LFU缓存
 */
public class LFU缓存 {

    public static void main(String[] args) {
        /*LFUCache lfuCache = new LFUCache(2);
        lfuCache.put(1, 1);
        lfuCache.put(2, 2);
        lfuCache.get(1);
        lfuCache.put(3, 3);
        lfuCache.get(2);
        lfuCache.get(3);
        lfuCache.put(4, 4);
        lfuCache.get(1);
        lfuCache.get(3);
        lfuCache.get(4);*/

        LFUCache lfuCache = new LFUCache(1);
        lfuCache.put(2,1);
        lfuCache.get(2);
        lfuCache.put(3,2);
        lfuCache.get(2);
        lfuCache.get(3);
    }
    public static class LFUCache {

        private Map<Integer , Node> cacheMap;
        private int capacity;
        private Map<Integer, LinkedHashSet<Node>> freqMap;

        public LFUCache(int capacity) {
            if (capacity<=0){
                throw new IllegalArgumentException("容量不能小于等于0");
            }
            this.cacheMap = new HashMap<>();
            this.capacity = capacity;
            this.freqMap = new HashMap<>();
        }

        public int get(int key){
            if (!cacheMap.containsKey(key)){
                return -1;
            }
            Node node = cacheMap.get(key);
            update(node);
            return node.value;
        }

        public void put(int key,int value){
            if (capacity<=0){
                return ;
            }
            if (cacheMap.containsKey(key)){
                //缓存中存在更新频率
                Node node = cacheMap.get(key);
                node.value=value;
                update(node);
                return ;
            }else {
                //缓存中不存在
                if (cacheMap.size()+1>capacity){
                    removeLeastFrequent();
                }
                Node node = new Node(key, value);
                cacheMap.put(key,node);
                if (!freqMap.containsKey(1)){
                    freqMap.put(1,new LinkedHashSet<>());
                }
                freqMap.get(1).add(node);
            }
            return;
        }

        private void removeLeastFrequent(){
            int min=1;
            while (!freqMap.containsKey(min)&&!freqMap.isEmpty()){
                min++;
            }
            LinkedHashSet<Node> minFre = freqMap.get(min);
            Node next = minFre.iterator().next();
            minFre.remove(next);
            cacheMap.remove(next.key);
            if (minFre.isEmpty()){
                freqMap.remove(min);
            }
        }
        private void update(Node node){
            int freq = node.freq;
            freqMap.get(freq).remove(node);
            if (freqMap.get(freq).isEmpty()){
                freqMap.remove(freq);
            }
            node.freq++;
            if (!freqMap.containsKey(node.freq)){
                freqMap.put(node.freq,new LinkedHashSet<>());
            }
            freqMap.get(node.freq).add(node);
        }

        public static class Node {
            int key;
            int value;
            int freq;

            public Node(int key, int value) {
                this.key = key;
                this.value = value;
                this.freq = 1;
            }
        }
    }
}
