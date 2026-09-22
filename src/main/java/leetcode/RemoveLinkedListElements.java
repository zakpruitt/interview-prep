package leetcode;

/**
 * 203. Remove Linked List Elements (Easy)
 *
 * https://leetcode.com/problems/remove-linked-list-elements/
 * Topics: Linked List, Recursion
 *
 * Given the head of a linked list and an integer val, remove all the nodes of the linked list that has Node.val == val, and return the new head.
 *
 * Example 1:
 *
 * Input: head = [1,2,6,3,4,5,6], val = 6
 * Output: [1,2,3,4,5]
 *
 * Example 2:
 *
 * Input: head = [], val = 1
 * Output: []
 *
 * Example 3:
 *
 * Input: head = [7,7,7,7], val = 7
 * Output: []
 *
 * Constraints:
 *
 *   - The number of nodes in the list is in the range [0, 10^4].
 *
 *   - 1 <= Node.val <= 50
 *
 *   - 0 <= val <= 50
 */
public class RemoveLinkedListElements {
    public ListNode removeElements(ListNode head, int val) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;

        while (head != null) {
            // keep it: attach to the end of the result, then step onto it
            if (head.val != val) {
                tail.next = head;
                tail = tail.next;
            }
            head = head.next;
        }

        // the last kept node may still point at a removed one: 1 -> 2 -> 6
        tail.next = null;

        return dummy.next;
    }
}
