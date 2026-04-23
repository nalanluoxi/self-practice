package likou;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：随机链表的复制
 * @Date：2025/5/29 11:53
 * @Filename：随机链表的复制
 */
public class 随机链表的复制 {


// Definition for a Node.
static class  Node {
    int val;
    Node next;
    Node random;

    public Node(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }
}


    public Node copyRandomList(Node head) {
        if (head == null)
            return null;
        Map<Node, Node> map = new HashMap<>(); // 原节点 -> 新节点映射
        Node curr = head;
        // 第一次遍历：创建新节点并建立映射
        while (curr != null) {
            map.put(curr, new Node(curr.val));
            curr = curr.next;
        }
        // 第二次遍历：设置next和random指针
        curr = head;
        while (curr != null) {
            Node clone = map.get(curr);
            clone.next = map.get(curr.next);
            clone.random = map.get(curr.random);
            curr = curr.next;
        }
        return map.get(head);
    }

}
