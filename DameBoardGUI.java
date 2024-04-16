import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DameBoardGUI extends JFrame {
    private static final int SIZE = 8;
    private static final int CELL_SIZE = 80;
    private static final Color LIGHT_COLOR = new Color(255, 206, 158);
    private static final Color DARK_COLOR = new Color(209, 139, 71);
    private static final Color HIGHLIGHT_COLOR = new Color(131, 232, 252, 150);

    private JPanel[][] cells = new JPanel[SIZE][SIZE];
    private JLabel[][] pieces = new JLabel[SIZE][SIZE];

    private boolean isPieceSelected = false;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public DameBoardGUI() {
        setTitle("Dame Board");
        setSize(SIZE * CELL_SIZE, SIZE * CELL_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel boardPanel = new JPanel(new GridLayout(SIZE, SIZE));
        boardPanel.setPreferredSize(new Dimension(SIZE * CELL_SIZE, SIZE * CELL_SIZE));
        boardPanel.setBackground(Color.BLACK);

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                JPanel cell = new JPanel(null);
                cell.setBackground((row + col) % 2 == 0 ? LIGHT_COLOR : DARK_COLOR);
                cells[row][col] = cell;

                int finalRow = row;
                int finalCol = col;
                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (!isPieceSelected && pieces[finalRow][finalCol] != null) {
                            isPieceSelected = true;
                            selectedRow = finalRow;
                            selectedCol = finalCol;
                            highlightValidMoves(finalRow, finalCol);
                        } else if (isPieceSelected && pieces[finalRow][finalCol] == null &&
                                cell.getBackground() == HIGHLIGHT_COLOR) {
                            movePiece(selectedRow, selectedCol, finalRow, finalCol);
                        } else {
                            resetSelection();
                        }
                    }
                });

                boardPanel.add(cell);
            }
        }

        addPieces();

        add(boardPanel);
    }

    private void addPieces() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if ((row + col) % 2 != 0) {
                    if (row < 3) {
                        pieces[row][col] = createPiece(Color.BLACK);
                    } else if (row > 4) {
                        pieces[row][col] = createPiece(Color.WHITE);
                    }
                }
            }
        }
    }

    private JLabel createPiece(Color color) {
        JLabel piece = new JLabel();
        piece.setSize(CELL_SIZE / 2, CELL_SIZE / 2);
        piece.setBackground(color);
        piece.setOpaque(true);
        piece.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        return piece;
    }

    private void highlightValidMoves(int row, int col) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if ((i + j) % 2 != 0 && pieces[i][j] == null) {
                    int dx = Math.abs(i - row);
                    int dy = Math.abs(j - col);
                    if (dx == 1 && dy == 1) {
                        if (pieces[row][col].getBackground() == Color.BLACK && i > row)
                            cells[i][j].setBackground(HIGHLIGHT_COLOR);
                        else if (pieces[row][col].getBackground() == Color.WHITE && i < row)
                            cells[i][j].setBackground(HIGHLIGHT_COLOR);
                    }
                }
            }
        }
    }

    private void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        pieces[toRow][toCol] = pieces[fromRow][fromCol];
        pieces[fromRow][fromCol] = null;
        cells[fromRow][fromCol].removeAll();
        cells[toRow][toCol].add(pieces[toRow][toCol]);
        resetSelection();
    }

    private void resetSelection() {
        isPieceSelected = false;
        selectedRow = -1;
        selectedCol = -1;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if ((i + j) % 2 != 0 && cells[i][j].getBackground() == HIGHLIGHT_COLOR) {
                    cells[i][j].setBackground((i + j) % 2 == 0 ? LIGHT_COLOR : DARK_COLOR);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DameBoardGUI dameBoardGUI = new DameBoardGUI();
            dameBoardGUI.setVisible(true);
        });
    }
}
