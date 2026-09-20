package leetcode;

import java.util.*;

public class FindNonRepeatingCharacters {

    public char findNonRepeatingCharacters(String input) {
        LinkedHashMap<Character, Integer> map = new LinkedHashMap<>();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            map.put(c, map.getOrDefault(c, 0) + 1);
        }

        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            char key = entry.getKey();
            Integer value = entry.getValue();
            if (value == 1) {
                return key;
            }
        }

        // no unique character in the input
        throw new IllegalArgumentException("every character repeats: " + input);
    }

}
