public class RemoveDuplicates {

    // https://leetcode.com/explore/interview/card/top-interview-questions-easy/92/array/727/

    // [1, 1, 2, 2, 3, 3, 3, 4]
    //  ^  ^

    // we can assume nums[0] is our first unique element, so we can compare back to there
    // and safely write to the next element

    public int removeDuplicates(int[] nums) {
        int w = 1;
        for (int r = 1; r < nums.length; r++) {
            if (nums[r] != nums[w - 1]) {
                nums[w] = nums[r];
                w++;
            }
        }
        return w;
    }
}


