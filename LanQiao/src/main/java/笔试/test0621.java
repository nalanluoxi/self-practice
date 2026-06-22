package 笔试;

import luogu.四平方定理;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class test0621 {

    public static void main(String[] args) {
       // System.out.println(DoubleNumber(1000));
        String str="A%sC%sE";
        char[]c={'B','D','F'};
        System.out.println(formatString(str,c));
    }



    public static class Solution {


        int size;

        Map<Integer,Node> map;

        Node head,tail;

        public Solution(int capacity) {
            // write code here
            size=capacity;
            map=new HashMap<>();
            head=new Node(-1,-1);
            tail=new Node(-1,-1);
            head.next=tail;
            tail.befor=head;
        }

        private void update(Node node){
            node.befor.next=node.next;
            node.next.befor=node.befor;

            Node next = head.next;
            node.befor=head;
            head.next=node;
            node.next=next;
            next.befor=node;
        }


        private void removeLast(){
            Node remove = tail.befor;
            map.remove(remove.key);

            remove.befor.next=tail;
            tail.befor=remove.befor;
        }

        public int get(int key) {
            // write code here
            if (map.containsKey(key)){
                Node node = map.get(key);
                update(node);
                return node.val;
            }
            return -1;
        }

        public void set(int key, int value) {
            // write code here
            if (map.containsKey(key)){
                Node node = map.get(key);
                node.val=value;
                map.put(key,node);
                update(node);
            }else {
                if (map.size()==size){
                    removeLast();
                }
                Node node = new Node(key, value);
                map.put(key,node);
                Node next = head.next;
                head.next=node;
                node.befor=head;
                next.befor=node;
                node.next=next;

            }
        }

        class Node{
            int key,val;
            Node next,befor;

            public Node(int key, int val) {
                this.key = key;
                this.val = val;
            }

            public Node() {
            }
        }
    }



    public static String formatString (String str, char[] arg) {
        // write code here
        StringBuffer sb=new StringBuffer();
        int j=0,i=0;
        int len = str.length();
        while (i<len){
            char c = str.charAt(i);
            if (c>='A' && c<='Z'){
                sb.append(c+"");
                i++;
            }else {
                i+=2;
                sb.append(arg[j]+"");
                j++;
            }
        }
        while (j<arg.length){
            sb.append(arg[j++]);
        }

        return sb.toString();
    }

    public static int DoubleNumber (long n) {
        // write code here

        set=new HashSet<>();

        init();
        int ans=0;
        for (long i = 1; i < n; i++) {
            String s = String.valueOf(i);
            if (s.length()%2==1){
                i=i*10;
                continue;
            }
            if (set.contains(i)){
                ans++;
            }
        }
        return  ans;
    }

    static Set<Long> set;
    public static void init(){
        long i=1;
        long max=999999;
        for (;i<=max;i++){
            String s = String.valueOf(i);
            String t = s + s;
            Long l = Long.valueOf(t);
            set.add(l);
        }
    }


}
