package example.java2.MyHashMapTest;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2
 * @Project：LanQiaoBei
 * @name：MyHashMap
 * @Date：2025/4/20 17:09
 * @Filename：MyHashMap
 */
public class MyHashMap<K, V> {
    private int capacity; // 容量

    private int size;//实际个数
    private  static final   double loadFactor=2/3.0;//负载因子

    private Node<K, V>[] table;//数组

    public MyHashMap() {
        this.capacity = 16;
        this.size = 0;
        this.table = new Node[capacity];
    }

    public int size() {
        return size;
    }

    public boolean remove(K key) {
        int hash = hash(key);
        if (table[hash] == null) {
            return false;
        } else {
            Node<K, V> tNode = table[hash];
            if (tNode.getKey().equals(key)) {
                table[hash] = tNode.next;
                size--;
                return true;
            }
            while (!tNode.next.getKey().equals(key) && tNode.next != null) {
                tNode = tNode.next;
            }
            if (tNode.next == null) {
                return false;
            } else if (tNode.next.getKey().equals(key)) {
                tNode.next = tNode.next.next;
            }
        }
        return true;
    }

    public boolean containsKey(K key) {
        int hash = hash(key);
        if (table[hash]!=null){
            Node<K, V> tNode = table[hash];
            while (tNode!=null){
                if (tNode.getKey().equals(key)){
                    return true;
                }
                tNode=tNode.next;
            }
        }
        return false;
    }

    public V get(K key) {
        int hash = hash(key);
        if (table[hash] == null) {
            return null;
        }else {
            Node<K, V> tNode = table[hash];
            while (tNode!=null){
                if (tNode.getKey().equals(key)){
                    return tNode.getValue();
                }
                tNode=tNode.next;
            }
        }
        return null;
    }

    public boolean put(K key, V value) {
        //System.out.println("loadFactor：" + this.loadFactor);
        int hash = hash(key);
        if (table[hash] == null) {
            table[hash] = new Node<>(key, value);
            size++;
        } else {
            Node<K, V> node = table[hash];
            while (node.next != null) {
                if (node.getKey().equals(key)) {
                    return false;
                }
                node = node.next;
            }
            if (node.getKey().equals(key)) {
                return false;
            }
            node.next = new Node<>(key, value);
            size++;
        }
        //System.out.println("size：" + size+" capacity：" + capacity);
        //System.out.println("size/capacity：" + (double)(size / capacity)+" loadFactor：" + loadFactor);
        if (size >= loadFactor*capacity) {
            expansion();
            //System.out.println("key：" + key + " value：" + value );
        }
        return true;
    }

    private void expansion() {
        int newcapacity = capacity * 2;
        Node<K, V>[] newTable = new Node[newcapacity];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> tNode = table[i];
            while (tNode != null) {
                int oldHash = hash(tNode.getKey());
                if ((oldHash & capacity) == 0) {
                    newTable[oldHash] = new Node<>(tNode.getKey(), tNode.getValue(), newTable[oldHash]);
                } else {
                    newTable[oldHash + capacity] = new Node<>(tNode.getKey(), tNode.getValue(), newTable[oldHash + capacity]);
                }
                tNode = tNode.next;
            }
        }
        table = newTable;
        capacity = newcapacity;
        System.out.println("扩容成功");
    }

    private boolean isEmpty() {
        return size == 0;
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    private class Node<K, V> {
        private K key;//键
        private V value;//值
        private Node<K, V> next;//下一个节点

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public Node() {
        }
    }


}
