package leetcode;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Valid Parentheses -- https://leetcode.com/problems/valid-parentheses/
 */
class ValidParenthesesTest {

    private final ValidParentheses solution = new ValidParentheses();

    @Test
    void acceptsMatchedPairs() {
        assertTrue(solution.isValid("()"));
        assertTrue(solution.isValid("()[]{}"));
        assertTrue(solution.isValid("([])"));
        assertTrue(solution.isValid("({[]})"));
    }

    @Test
    void rejectsMismatchedTypes() {
        assertFalse(solution.isValid("(]"));
        assertFalse(solution.isValid("([)]"));
    }

    @Test
    void rejectsUnclosedOpeners() {
        assertFalse(solution.isValid("("));
        assertFalse(solution.isValid("(["));
    }

    @Test
    void rejectsCloserWithNothingOpen() {
        assertFalse(solution.isValid("]"));
        assertFalse(solution.isValid("(){}}{"));
    }
}
