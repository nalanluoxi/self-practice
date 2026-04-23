package likou.力扣test2.前缀树;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2.前缀树
 * @Project：LanQiaoBei
 * @name：Trie1
 * @Date：2025/7/14 11:16
 * @Filename：Trie1
 */
public class Trie1 {
    class Trie {
        private Trie[] children;
        private boolean isEnd;

        public Trie() {
            children=new Trie[26];
            isEnd=false;
        }

        public void insert(String word) {
            Trie node=this;
            for (int i = 0; i < word.length(); i++) {
                int index = word.charAt(i) - 'a';
                if (node.children[index]==null){
                    node.children[index]=new Trie();
                }
                node=node.children[index];
            }
            node.isEnd=true;
        }

        public boolean search(String word) {
            Trie trie = searchPre(word);
            return trie!=null && trie.isEnd;
        }

        public Trie searchPre(String pre){
            Trie node=this;
            for (int i = 0; i < pre.length(); i++) {
                int index = pre.charAt(i) - 'a';
                if (node.children[index]==null){
                    return null;
                }
                node=node.children[index];
            }
            return node;
        }

        public boolean startsWith(String prefix) {
            return searchPre(prefix)!=null;
        }
    }
}
