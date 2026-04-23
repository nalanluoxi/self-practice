package likou.LFU算法;

import example.java2.IFo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * @Author 纳兰洛熙
 * @Package：likou.LFU算法
 * @Project：LanQiaoBei
 * @name：MyLFU
 * @Date：2025/5/18 16:28
 * @Filename：MyLFU
 */
public class MyLFU {

    private Map<String ,Node> cacheMap;
    private int capacity,minfreq;
    private Map<Integer, LinkedHashSet<Node>> freqMap;

    public MyLFU(int capacity) {
        if (capacity<=0){
            throw new IllegalArgumentException("容量不能小于等于0");
        }
        this.cacheMap = new HashMap<>();
        this.capacity = capacity;
        this.minfreq = 0;
        this.freqMap = new HashMap<>();
    }

    public String get(String key){
        if (!cacheMap.containsKey(key)){
            return null;
        }
        Node node = cacheMap.get(key);
        update(node);
        return node.value;
    }

    public Boolean set(String key,String value){
        if (capacity<=0){
            return false;
        }
        if (cacheMap.containsKey(key)){
            //缓存中存在更新频率
            Node node = cacheMap.get(key);
            node.value=value;
            update(node);
            return true;
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
            minfreq=1;
        }
        return true;
    }

    private void removeLeastFrequent(){
        LinkedHashSet<Node> minFre = freqMap.get(minfreq);
        Node next = minFre.iterator().next();
        minFre.remove(next);
        cacheMap.remove(next.key);
        if (minFre.isEmpty()){
            freqMap.remove(minfreq);
        }
    }
    private void update(Node node){
        int freq = node.freq;
        freqMap.get(freq).remove(node);
        if (freqMap.get(freq).isEmpty()){
            freqMap.remove(freq);
            if (freq==minfreq && !freqMap.isEmpty()){
                int i=freq+1;
                while (!freqMap.containsKey(i)){
                    i++;
                }
            }
        }
        node.freq++;
        if (!freqMap.containsKey(node.freq)){
            freqMap.put(node.freq,new LinkedHashSet<>());
        }
        freqMap.get(node.freq).add(node);
    }

    private class Node {
        String key;
        String value;
        int freq;

        public Node(String key, String value) {
            this.key = key;
            this.value = value;
            this.freq = 1;
        }
    }
}
