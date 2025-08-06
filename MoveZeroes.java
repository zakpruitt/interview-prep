import java.util.ArrayList;
import java.util.List;

public class MoveZeroes {

    // https://leetcode.com/explore/interview/card/top-interview-questions-easy/92/array/567/

    public void moveZeroes(int[] nums) {
        List<Integer> nonZeroNumbers = new ArrayList<>(); // preserves order
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) nonZeroNumbers.add(nums[i]);
        }

        for (int i = 0; i < nums.length; i++) {
            nums[i] = 0;
        }
        for (int i = 0; i < nonZeroNumbers.size(); i++) {
            nums[i] = nonZeroNumbers.get(i);
        }
    }

    // two pointer solt
    public void moveZeroesTwoPointer(int[] nums) {
        int j = 0; // Pointer to place the next non-zero element
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                // Swap current element with the element at index j
                int temp = nums[i];
                nums[i] = nums[j];
                nums[j] = temp;
                j++;   // Move j to the next index for placing non-zero
            }
        }
    }
}
