import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TwoSum {

    // https://leetcode.com/explore/interview/card/top-interview-questions-easy/92/array/546/

    public int[] twoSum(int[] nums, int target) throws Exception {
        Map<Integer, Integer> seen = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int difference = target - nums[i];
            if (seen.containsKey(difference)) {
                return new int[]{seen.get(difference), i};
            }
            seen.put(nums[i], i);
        }

        return new int[]{1, 1};
    }

}
