import java.util.Arrays;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;

public class ParallelConways {
    public static void main(String[] args) throws InterruptedException {
        final int M = 15;
        final int N = 15;
        boolean[][] board = new boolean[M][N];
        boolean[][] nextBoard = new boolean[M][N];
        Phaser ph0 = new Phaser(1);
        Phaser ph1 = new Phaser(M * N);
        int gen = 0;

        // Populate 40% of the board
        for (int i = 0; i < (M * N * 0.4); i++) {
            board[ThreadLocalRandom.current().nextInt(0, M)][ThreadLocalRandom.current().nextInt(0, N)] = true;
        }

        showBoard(board, gen);

        // Start all cell threads
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                new Thread(new CellThread(ph0, ph1, board, nextBoard, i, j)).start();
            }
        }

        while (Thread.currentThread().isAlive()) {
            ph1.awaitAdvance(ph1.getPhase());
            updateBoard(board, nextBoard);
            showBoard(board, ++gen);
            ph0.arrive();
            Thread.sleep(1000);
        }
    }
    public static void showBoard(boolean[][] board, int gen) {
        System.out.println("Generation: " + gen);
        for (int i = 0; i < board.length; i++) {
            System.out.print("[");
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j]) {
                    System.out.print(" x ");
                }
                else {
                    System.out.print("   ");
                }
            }
            System.out.println("]\n");
        }
    }

    public static void updateBoard(boolean[][] board, boolean[][] nextBoard) {
        for (int i = 0; i < board.length; i++) {
            board[i] = Arrays.copyOfRange(nextBoard[i], 0, board[i].length);
        }
    }
}
