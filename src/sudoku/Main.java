package sudoku;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SudokuBoard board = new SudokuBoard();

        for (String arg : args) {
            String[] parts = arg.split(",");
            if (parts.length == 3) {
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);
                int val = Integer.parseInt(parts[2]);
                board.setFixed(row, col, val);
            }
        }

        SwingUtilities.invokeLater(() -> new SudokuGUI(board));
    }
}
