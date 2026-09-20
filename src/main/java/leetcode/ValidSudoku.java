package leetcode;

import java.util.HashSet;
import java.util.Set;

public class ValidSudoku {

    // https://leetcode.com/problems/valid-sudoku/description/

    public boolean isValidSudoku(char[][] board) {
        Set<Character>[] rows = new HashSet[9];
        Set<Character>[] cols = new HashSet[9];
        Set<Character>[][] boxes = new HashSet[3][3];

        for (int i = 0; i < 9; i++) {
            rows[i] = new HashSet<>();
            cols[i] = new HashSet<>();
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boxes[i][j] = new HashSet<>();
            }
        }

        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                char val = board[r][c];
                if (val == '.') continue;

                int rowBand = r / 3;
                int colBand = c / 3;

                if (rows[r].contains(val)) return false;
                rows[r].add(val);

                if (cols[c].contains(val)) return false;
                cols[c].add(val);

                if (boxes[rowBand][colBand].contains(val)) return false;
                boxes[rowBand][colBand].add(val);
            }
        }

        return true;
    }
}
