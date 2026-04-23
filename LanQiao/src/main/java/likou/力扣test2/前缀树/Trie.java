package likou.力扣test2.前缀树;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2.前缀树
 * @Project：LanQiaoBei
 * @name：Trie
 * @Date：2025/7/12 11:02
 * @Filename：Trie
 */
public class Trie {

    private Trie[] children;
    private boolean isEnd;
    public Trie() {
        children=new Trie[26];
        isEnd=false;
    }

    public void insert(String word) {
        Trie node=this;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int index = c - 'a';
            if (children[index]==null){
                node.children[index]=new Trie();
            }
            node=node.children[index];
        }
        node.isEnd=true;
    }

    public boolean search(String word) {
        Trie node= searchPrefix(word);
        return node!=null&&node.isEnd;
    }

    private Trie searchPrefix(String word) {
        Trie node = this;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int index = c - 'a';
            if (node.children[index]==null){
                return null;
            }
            node=node.children[index];
        }
        return node;
    }

    public boolean startsWith(String prefix) {
        return searchPrefix(prefix)!=null;
    }
}
