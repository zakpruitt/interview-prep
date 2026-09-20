package leetcode;

/** LeetCode's singly-linked list node, shared by every linked-list problem here. */
public class ListNode {
    public int val;
    public ListNode next;

    public ListNode() {
    }

    public ListNode(int val) {
        this.val = val;
    }

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    /** Builds a list from values, so tests can say ListNode.of(1, 2, 3). */
    public static ListNode of(int... values) {
        ListNode head = null;
        for (int i = values.length - 1; i >= 0; i--) {
            head = new ListNode(values[i], head);
        }
        return head;
    }

    /** Flattens back to an array, which makes assertArrayEquals easy. */
    public static int[] toArray(ListNode head) {
        int size = 0;
        for (ListNode node = head; node != null; node = node.next) {
            size++;
        }
        int[] values = new int[size];
        int i = 0;
        for (ListNode node = head; node != null; node = node.next) {
            values[i++] = node.val;
        }
        return values;
    }
}
