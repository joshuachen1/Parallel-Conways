import java.util.concurrent.Phaser;

public class CellThread implements Runnable {
    private Phaser ph0;
    private Phaser ph1;
    private boolean[][] board;
    private boolean[][] nextBoard;
    private int row, col;

    public CellThread(Phaser ph0, Phaser ph1, boolean[][] board, boolean[][] nextBoard, int row, int col) {
        this.ph0 = ph0;
        this.ph1 = ph1;
        this.board = board;
        this.nextBoard = nextBoard;
        this.row = row;
        this.col = col;
    }

    public int checkNeighbors () {
        int numNeighbors = 0;

        try {
            // Upper Left
            if (board[row - 1][col - 1])
                numNeighbors += 1;
        } catch (ArrayIndexOutOfBoundsException e) {}

        try {
            // Above
            if (board[row - 1][col])
                numNeighbors += 1;
        } catch (ArrayIndexOutOfBoundsException e) {}

        try {
            // Upper Right
            if (board[row - 1][col + 1])
                numNeighbors += 1;
        } catch (ArrayIndexOutOfBoundsException e) {}

        try {
            // Left
            if (board[row][col - 1])
                numNeighbors += 1;
        } catch (ArrayIndexOutOfBoundsException e) {}

        try {
            // Right
            if (board[row][col + 1])
                numNeighbors += 1;
        } catch (ArrayIndexOutOfBoundsException e) {}

        try {
            // Lower Left
            if (board[row + 1][col - 1])
                numNeighbors += 1;
        } catch (ArrayIndexOutOfBoundsException e) {}

        try {
            // Below
            if (board[row + 1][col])
                numNeighbors += 1;
        } catch (ArrayIndexOutOfBoundsException e) {}

        try {
            // Lower Right
            if (board[row + 1][col + 1])
                numNeighbors += 1;
        } catch (ArrayIndexOutOfBoundsException e) {}

        return numNeighbors;
    }

    @Override
    public void run() {
        while (true) {
            boolean isAlive = board[row][col];
            int numNeighbors = this.checkNeighbors();

            if (isAlive) {
                // Underpopulation or Overpopulation
                if (numNeighbors < 2 || numNeighbors > 3) {
                    isAlive = false;
                }
                // Survive Generation
                else {
                    isAlive = true;
                }
            }
            else {
                // Reproduction
                if (numNeighbors == 3) {
                    isAlive = true;
                }
            }

            nextBoard[row][col] = isAlive;

            // System.out.println(Thread.currentThread().getName() + " has arrived.");

            // Notify completion of cell task
            ph1.arrive();
            ph0.awaitAdvance(ph0.getPhase());

            try {
                // Reset board
                nextBoard[row][col] = false;
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
        }
    }
}
