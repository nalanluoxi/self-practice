package example.java2.MyBloomFilter;

import java.util.BitSet;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2.MyBoolFilter
 * @Project：LanQiaoBei
 * @name：ExpandBoolFilter
 * @Date：2025/5/15 19:55
 * @Filename：ExpandBoolFilter
 */
public class ExpandBloomFilter {
    private long count;
    private long bitSetSize;
    private BitSet bitSet;

    private int hashCount;

    private int p;

   /* public ExpandBloomFilter( int size, int hashCount) {
        this.size = size;
        this.bitSet = new BitSet(size);
        this.hashCount = hashCount;
    }
*/

    private long getCount(long n, double p) {
        if (p == 0.0) {
            p = Double.MIN_VALUE;
        }
        return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }
    public  int getHashCount(long n, long m) {
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }

    public ExpandBloomFilter(int p,int count) {
        this.p = p;
        this.count = count;
        this.bitSetSize = (int) getCount(count, p);
        this.bitSet=new BitSet((int) bitSetSize);
    }

    private int hash1(Object key){
        return (int) ((int) (Math.abs(key.hashCode()))%bitSetSize);
    }

    private int hash2(Object key){
        int h;
        int i = key == null ? 0 : (h = key.hashCode()) ^ h >>> 16;
        return (int) Math.abs(i%bitSetSize);
    }

    public void add(Object key){
        int hash1 = hash1(key);
        int hash2 = hash2(key);
        for (int i = 0; i < hashCount; i++) {
            bitSet.set(hash1);
            hash1+=hash2;
        }
        count++;
    }
    public boolean check(Object key){
        int hash1 = hash1(key);
        int hash2 = hash2(key);
        for (int i = 0; i < hashCount; i++) {
            if (!bitSet.get(hash1)){
                return false;
            }
            hash1+=hash2;
        }
        return true;
    }

}
