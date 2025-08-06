public class PlusOne {

    // https://leetcode.com/explore/interview/card/top-interview-questions-easy/92/array/559/

    public int[] plusOne(int[] digits) {
        for (int i = digits.length - 1; i >= 0; i--) {
            int newDigit = digits[i] + 1;
            if (newDigit > 9) {
                // we have to carry
                digits[i] = 0;
            } else {
                digits[i] += 1;
                return digits;
            }
        }

        int leadingValue = digits[0] + 1;
        int[] b = new int[digits.length + 1];
        b[0] = leadingValue;
        for (int i = 1; i < b.length; i++) {
            b[i] = 0;
        }

        return b;
    }
}
