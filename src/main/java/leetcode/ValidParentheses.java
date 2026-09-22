package leetcode;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 20. Valid Parentheses (Easy)
 *
 * https://leetcode.com/problems/valid-parentheses/
 * Topics: String, Stack, Bracket Sequences
 *
 *  ({[]})
 *
 *
 * Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.
 *
 * An input string is valid if:
 *
 *   - Open brackets must be closed by the same type of brackets.
 *
 *   - Open brackets must be closed in the correct order.
 *
 *   - Every close bracket has a corresponding open bracket of the same type.
 *
 * Example 1:
 *
 * Input: s = "()"
 *
 * Output: true
 *
 * Example 2:
 *
 * Input: s = "()[]{}"
 *
 * Output: true
 *
 * Example 3:
 *
 * Input: s = "(]"
 *
 * Output: false
 *
 * Example 4:
 *
 * Input: s = "([])"
 *
 * Output: true
 *
 * Example 5:
 *
 * Input: s = "([)]"
 *
 * Output: false
 *
 * Constraints:
 *
 *   - 1 <= s.length <= 10^4
 *
 *   - s consists of parentheses only '()[]{}'.
 */
public class ValidParentheses {

    public boolean isValid(String s) {
        // Push the closer we expect to see, not the opener itself. That way the
        // closing case has nothing to translate: pop and compare to the char.
        Deque<Character> expected = new ArrayDeque<>();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '(':
                    expected.push(')');
                    break;
                case '{':
                    expected.push('}');
                    break;
                case '[':
                    expected.push(']');
                    break;
                default:
                    // A closer with nothing open, or the wrong closer, both fail
                    if (expected.isEmpty() || expected.pop() != c) {
                        return false;
                    }
            }
        }

        // Anything still open never got closed: "(" and "([" are invalid.
        return expected.isEmpty();
    }

}
