public class RotateArray {

    // https://leetcode.com/explore/interview/card/top-interview-questions-easy/92/array/646/


    // 1, 2, 3, 4, 5      4
    // length - 1 = 4
    //
    //

    public void rotate(int[] nums, int k) {
        int n = nums.length;
        int[] rotatedNums = new int[n];

        for (int i = 0; i < n; i++) {
            int newIndex = (i + k) % n;
            rotatedNums[newIndex] = nums[i];
        }

        // copy back into the original array
        for (int i = 0; i < n; i++) {
            nums[i] = rotatedNums[i];
        }
    }
}
