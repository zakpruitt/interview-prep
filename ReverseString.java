public class ReverseString {

    // https://leetcode.com/explore/interview/card/top-interview-questions-easy/127/strings/879/

    public void reverseString(char[] s) {
        int endIndex = s.length - 1;
        for (int i = 0; i < s.length / 2; i++) {
            char tempVariable = s[i];
            s[i] = s[endIndex];
            s[endIndex] = tempVariable;

            endIndex--;
        }
    }
}
