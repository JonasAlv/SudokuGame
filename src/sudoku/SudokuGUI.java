package sudoku;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

public class SudokuGUI extends JFrame {
    private SudokuBoard sudoku;
    private JButton[][] buttons = new JButton[9][9];

    public SudokuGUI(SudokuBoard board) {
        this.sudoku = board;
        setTitle("Sudoku");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel grid = new JPanel(new GridLayout(9, 9));
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JButton btn = new JButton();
                int row = i, col = j;
                btn.setFont(new Font("Arial", Font.BOLD, 20));
                btn.addActionListener(e -> cellClicked(row, col));
                buttons[i][j] = btn;
                grid.add(btn);
            }
        }

        JPanel menu = new JPanel(new GridLayout(7, 1));
        String[] options = {
                "1. Iniciar jogo", "2. Colocar número", "3. Remover número",
                "4. Verificar jogo", "5. Verificar status", "6. Limpar", "7. Finalizar"
        };
        for (String option : options) {
            JButton b = new JButton(option);
            b.addActionListener(e -> handleOption(option));
            menu.add(b);
        }

        add(grid, BorderLayout.CENTER);
        add(menu, BorderLayout.EAST);
        updateBoard();
        setVisible(true);
    }

    private void updateBoard() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                SudokuCell cell = sudoku.getCell(i, j);
                int val = cell.getValue();
                buttons[i][j].setText(val == 0 ? "" : String.valueOf(val));
                buttons[i][j].setForeground(cell.isFixed() ? Color.BLUE : Color.BLACK);
            }
    }

    private void cellClicked(int row, int col) {
        SudokuCell cell = sudoku.getCell(row, col);
        if (cell.isFixed()) {
            JOptionPane.showMessageDialog(this, "Número fixo não pode ser alterado!");
            return;
        }
        String input = JOptionPane.showInputDialog(this,
                "Digite número (1-9) ou notas separadas por vírgula:");
        if (input == null || input.isEmpty()) return;

        if (input.contains(",")) {
            HashSet<Integer> notes = new HashSet<>();
            for (String s : input.split(",")) {
                try { notes.add(Integer.parseInt(s.trim())); } catch (Exception ignored) {}
            }
            cell.setNotes(notes);
        } else {
            try {
                int val = Integer.parseInt(input);
                if (val >= 1 && val <= 9) cell.setValue(val);
            } catch(Exception ignored) {}
        }
        updateBoard();
    }

    private void handleOption(String option) {
        switch(option) {
            case "1. Iniciar jogo" -> updateBoard();
            case "2. Colocar número" -> placeNumber();
            case "3. Remover número" -> removeNumber();
            case "4. Verificar jogo" -> updateBoard();
            case "5. Verificar status" -> checkStatus();
            case "6. Limpar" -> { sudoku.clearUserValues(); updateBoard(); }
            case "7. Finalizar" -> finalizeGame();
        }
    }

    private void placeNumber() {
        try {
            int row = Integer.parseInt(JOptionPane.showInputDialog(this, "Linha 0-8"));
            int col = Integer.parseInt(JOptionPane.showInputDialog(this, "Coluna 0-8"));
            int val = Integer.parseInt(JOptionPane.showInputDialog(this, "Número 1-9"));
            if (!sudoku.setValue(row, col, val))
                JOptionPane.showMessageDialog(this, "Não é possível colocar nesse local!");
            updateBoard();
        } catch(Exception ignored) {}
    }

    private void removeNumber() {
        try {
            int row = Integer.parseInt(JOptionPane.showInputDialog(this, "Linha 0-8"));
            int col = Integer.parseInt(JOptionPane.showInputDialog(this, "Coluna 0-8"));
            if (!sudoku.removeValue(row, col))
                JOptionPane.showMessageDialog(this, "Número fixo não pode ser removido!");
            updateBoard();
        } catch(Exception ignored) {}
    }

    private void checkStatus() {
        String status = "Status: ";
        if (!sudoku.isComplete()) status += "Incompleto";
        else status += "Completo";
        status += sudoku.hasErrors() ? " (com erros)" : " (sem erros)";
        JOptionPane.showMessageDialog(this, status);
    }

    private void finalizeGame() {
        if (!sudoku.isComplete())
            JOptionPane.showMessageDialog(this, "Preencha todos os espaços!");
        else if (sudoku.hasErrors())
            JOptionPane.showMessageDialog(this, "O jogo contém erros!");
        else {
            JOptionPane.showMessageDialog(this, "Parabéns! Jogo finalizado corretamente!");
            System.exit(0);
        }
    }
}
