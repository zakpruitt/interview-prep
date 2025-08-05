public class BestTimeToBuyAndSellStock2 {

    // https://leetcode.com/explore/interview/card/top-interview-questions-easy/92/array/564/

    // int[] stockPrices = [7,1,5,3,6,4];
    // int[] stockPrices = [1,2,3,4,1,2];
    // this one is simple when you visualize it
    // since we can look in the future, just buy low, sell high

    public static int maxProfit(int[] prices) {
        int profit = 0;

        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1]) {
                profit += (prices[i] - prices[i - 1]);
                System.out.print("we bought at " + prices[i - 1] + " and sold at " + prices[i]);
            }
            System.out.println();
        }

        return profit;
    }

    public static void main(String[] args) {
        int[] stockPrices = {1, 2, 3, 4, 1, 2, 10, 1, 1, 1, 1, 1, 2, 3};
        maxProfit(stockPrices);
    }
}
