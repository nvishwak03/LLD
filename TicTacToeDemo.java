
// Cell : row, col, value(' ', 'X', 'O'), constructor(), isEmpty()
// Board : 3x3 grid, constructor(), place(row,col,player)->boolean, checkWin(player)->boolean, isFull()->boolean, print()
// Game : board, currentPlayer('X'/'O'), constructor(), move(row,col)->boolean, switchTurn(), status(), reset()
// Demo : play a few moves

class Cell {
    int row, col;
    char value; // ' ', 'X', or 'O'

    public Cell(int row, int col) {
        this.row = row; this.col = col;
        this.value = ' ';
    }

    public boolean isEmpty() { return value == ' '; }
}

// ----- Board -----
class Board {
    Cell[][] grid = new Cell[3][3];

    public Board() {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                grid[r][c] = new Cell(r, c);
    }

    public boolean place(int r, int c, char player) {
        if (r < 0 || r >= 3 || c < 0 || c >= 3) return false;
        if (!grid[r][c].isEmpty()) return false;
        grid[r][c].value = player;
        return true;
    }

    public boolean checkWin(char p) {
        // rows & cols
        for (int i = 0; i < 3; i++) {
            if (grid[i][0].value == p && grid[i][1].value == p && grid[i][2].value == p) return true;
            if (grid[0][i].value == p && grid[1][i].value == p && grid[2][i].value == p) return true;
        }
        // diagonals
        if (grid[0][0].value == p && grid[1][1].value == p && grid[2][2].value == p) return true;
        if (grid[0][2].value == p && grid[1][1].value == p && grid[2][0].value == p) return true;
        return false;
    }

    public boolean isFull() {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                if (grid[r][c].isEmpty()) return false;
        return true;
    }

    public void print() {
        System.out.println("+-+-+-+");
        for (int r = 0; r < 3; r++) {
            System.out.println("|" + grid[r][0].value + "|" + grid[r][1].value + "|" + grid[r][2].value + "|");
            System.out.println("+-+-+-+");
        }
        System.out.println();
    }
}

// ----- Game -----
class TicTacToe {
    Board board = new Board();
    char currentPlayer = 'X';
    boolean gameOver = false;

    public boolean move(int r, int c) {
        if (gameOver) { System.out.println("Game over. Reset to play again."); return false; }
        boolean ok = board.place(r, c, currentPlayer);
        if (!ok) { System.out.println("Invalid move at (" + r + "," + c + ")"); return false; }

        if (board.checkWin(currentPlayer)) {
            board.print();
            System.out.println("Player " + currentPlayer + " WINS!");
            gameOver = true;
            return true;
        }
        if (board.isFull()) {
            board.print();
            System.out.println("DRAW!");
            gameOver = true;
            return true;
        }
        switchTurn();
        return true;
    }

    private void switchTurn() { currentPlayer = (currentPlayer == 'X') ? 'O' : 'X'; }

    public void status() {
        System.out.println("Turn: Player " + currentPlayer + (gameOver ? " (game over)" : ""));
        board.print();
    }

    public void reset() {
        board = new Board();
        currentPlayer = 'X';
        gameOver = false;
    }
}

// ----- Demo -----
public class TicTacToeDemo {
    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        game.status();

        game.move(0,0); // X
        game.status();
        game.move(1,1); // O
        game.status();
        game.move(0,1); // X
        game.status();
        game.move(2,2); // O
        game.status();
        game.move(0,2); // X wins on top row
        // game.status(); // already printed on win
    }
}
