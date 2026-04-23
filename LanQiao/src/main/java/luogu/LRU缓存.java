package luogu;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：luogu
 * @Project：LanQiaoBei
 * @name：LRU缓存
 * @Date：2025/3/19 8:27
 * @Filename：LRU缓存
 */
public class LRU缓存 {
    public static void main(String[] args) {
        LRUCache l = new LRUCache(2);
        l.put(1,1);
        l.put(2,2);
        System.out.println(l.get(1));
        l.put(3,3);
        System.out.println(l.get(2));
        l.put(4,4);
        System.out.println(l.get(1));
        System.out.println(l.get(3));
        System.out.println(l.get(4));
    }

    static class LRUCache {

        public LRUCache(int capacity) {
            hash=new int[10001];
            Arrays.fill(hash,-1);
            //map=new HashMap<>();
            deque=new LinkedList<>();
            this.capacity=capacity;
            size=0;
        }
        private int[] hash;
        //private HashMap<Integer,Integer> map;
        private Deque<Integer> deque;
        private int capacity;
        private int size;

        public int get(int key) {
            if (hash[key]!=-1){
                deque.remove(key);
                deque.offerFirst(key);
                return hash[key];
            }
            return -1;
        }

        public void put(int key, int value) {
            if (hash[key]!=-1){
                hash[key]=value;
                deque.remove(key);
                deque.offerFirst(key);
                return;
            }else {
                if (size<capacity){
                    size++;
                    deque.offerFirst(key);
                    hash[key]=value;
                }else {
                    int last = deque.pollLast();
                    hash[last]=-1;
                    size--;
                    put(key,value);
                }
            }
        }
    }
}