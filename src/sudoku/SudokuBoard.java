package sudoku;

import java.util.HashSet;
import java.util.Set;
import java.util.Random;


public class SudokuBoard {
    private SudokuCell[][] board = new SudokuCell[9][9];

    public SudokuBoard() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                board[i][j] = new SudokuCell();
    }

    public SudokuCell getCell(int row, int col) { return board[row][col]; }

    public void setFixed(int row, int col, int value) {
        board[row][col].setValue(value);
        board[row][col].setFixed(true);
    }

    public boolean setValue(int row, int col, int value) {
        SudokuCell cell = board[row][col];
        if (cell.isFixed() || cell.getValue() != 0) return false;
        cell.setValue(value);
        return true;
    }

    public boolean removeValue(int row, int col) {
        SudokuCell cell = board[row][col];
        if (cell.isFixed()) return false;
        cell.setValue(0);
        return true;
    }

    public void clearUserValues() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (!board[i][j].isFixed()) board[i][j].setValue(0);
    }

    public boolean isComplete() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (board[i][j].getValue() == 0) return false;
        return true;
    }

    public boolean hasErrors() {
        for (int i = 0; i < 9; i++) {
            Set<Integer> rowSet = new HashSet<>();
            Set<Integer> colSet = new HashSet<>();
            for (int j = 0; j < 9; j++) {
                int rowVal = board[i][j].getValue();
                int colVal = board[j][i].getValue();
                if (rowVal != 0 && !rowSet.add(rowVal)) return true;
                if (colVal != 0 && !colSet.add(colVal)) return true;
            }
        }

        for (int br = 0; br < 3; br++) {
            for (int bc = 0; bc < 3; bc++) {
                Set<Integer> blockSet = new HashSet<>();
                for (int i = 0; i < 3; i++)
                    for (int j = 0; j < 3; j++) {
                        int val = board[br*3 + i][bc*3 + j].getValue();
                        if (val != 0 && !blockSet.add(val)) return true;
                    }
            }
        }
        return false;
    }

    public void generateRandomFixedNumbers(int count) {
        Random rand = new Random();
        int filled = 0;
        while (filled < count) {
            int row = rand.nextInt(9);
            int col = rand.nextInt(9);
            int val = rand.nextInt(9) + 1;
            SudokuCell cell = board[row][col];
            if (cell.getValue() == 0) {
                cell.setValue(val);
                if (!hasErrors()) {
                    cell.setFixed(true);
                    filled++;
                } else {
                    cell.setValue(0);
                }
            }
        }
    }

}
