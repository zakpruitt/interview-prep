package leetcode;

import java.util.HashMap;

/**
 * 242. Valid Anagram (Easy)
 *
 * https://leetcode.com/problems/valid-anagram/
 * Topics: Hash Table, String, Sorting
 *
 * Given two strings s and t, return true if t is an anagram of s, and false otherwise.
 *
 * Example 1:
 *
 * Input: s = "anagram", t = "nagaram"
 *
 * Output: true
 *
 * Example 2:
 *
 * Input: s = "rat", t = "car"
 *
 * Output: false
 *
 * Constraints:
 *
 *   - 1 <= s.length, t.length <= 5 * 10^4
 *
 *   - s and t consist of lowercase English letters.
 *
 * Follow up: What if the inputs contain Unicode characters? How would you adapt your solution to such a case?
 */
public class ValidAnagram {
    public boolean isAnagram(String s, String t) {
        if  (s.length() != t.length()) {
            return false;
        }

        HashMap<Character, Integer> map = new HashMap<>();

        // s counts each letter up, t counts it back down
        for (int i = 0; i < s.length(); i++) {
            map.put(s.charAt(i), map.getOrDefault(s.charAt(i), 0) + 1);
            map.put(t.charAt(i), map.getOrDefault(t.charAt(i), 0) - 1);
        }

        // balanced letters end at 0; anything else means s and t disagree
        for (int count : map.values()) {
            if (count != 0) {
                return false;
            }
        }
        return true;
    }
}
