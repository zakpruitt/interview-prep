public class SingleNumber {

    // https://leetcode.com/explore/interview/card/top-interview-questions-easy/92/array/549/

    public int singleNumber(int[] nums) {
        int result = 0;

        for (int num : nums) {
            result = result ^ num;
        }

        return result;
    }
}
