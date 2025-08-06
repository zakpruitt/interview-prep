import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntersectionOfTwoArrays2 {

    // https://leetcode.com/explore/interview/card/top-interview-questions-easy/92/array/674/

    public static int[] intersect(int[] nums1, int[] nums2) {
        Map<Integer, Integer> map = new HashMap<>();
        List<Integer> result = new ArrayList<>();

        // Count frequencies in nums1
        for (int num : nums1) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }

        // Check against nums2
        for (int num : nums2) {
            if (map.containsKey(num) && map.get(num) > 0) {
                result.add(num);
                map.put(num, map.get(num) - 1);
            }
        }

        // Convert result list to array
        int[] intersect = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            intersect[i] = result.get(i);
        }

        return intersect;
    }

    public static void main(String[] args) {
        intersect(new int[]{1, 2, 2, 1}, new int[]{2, 2});
    }

}
