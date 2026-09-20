package leetcode;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Two Sum -- https://leetcode.com/problems/two-sum/
 *
 * Template for the rest: one @Test per example, name it after what it covers.
 */
class TwoSumTest {

    private final TwoSum solution = new TwoSum();

    @Test
    void findsThePairAtTheFront() throws Exception {
        assertArrayEquals(new int[]{0, 1}, solution.twoSum(new int[]{2, 7, 11, 15}, 9));
    }

    @Test
    void findsThePairInTheMiddle() throws Exception {
        assertArrayEquals(new int[]{1, 2}, solution.twoSum(new int[]{3, 2, 4}, 6));
    }

    @Test
    void handlesDuplicateValues() throws Exception {
        assertArrayEquals(new int[]{0, 1}, solution.twoSum(new int[]{3, 3}, 6));
    }
}
