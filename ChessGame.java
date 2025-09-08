// Piece : type, color, row, col, constructor(), move()
// Cell : row, col, piece, constructor()
// Board : 8x8 cells, constructor(), get(), set(), printBoard()
// Game : board, currentPlayer, initBoard(), move(), switchTurn()

class Piece {
    String type;   // "PAWN", "ROOK", etc.
    String color;  // "WHITE" or "BLACK"
    int row, col;

    public Piece(String type, String color, int row, int col) {
        this.type = type; this.color = color; this.row = row; this.col = col;
    }

    public void move(int toR, int toC) {
        this.row = toR; this.col = toC;
    }

    public String toString() {
        return color.charAt(0) + type.substring(0,1); // e.g., WP = White Pawn
    }
}


class Cell {
    int row, col; Piece piece;
    public Cell(int row, int col) { this.row=row; this.col=col; }
}

class Board {
    Cell[][] grid = new Cell[8][8];

    public Board() {
        for (int r=0;r<8;r++)
            for (int c=0;c<8;c++)
                grid[r][c] = new Cell(r,c);
    }

    public Piece get(int r,int c){ return grid[r][c].piece; }
    public void set(int r,int c,Piece p){ grid[r][c].piece=p; }

    public void printBoard() {
        for (int r=0;r<8;r++) {
            for (int c=0;c<8;c++) {
                Piece p = get(r,c);
                System.out.print((p==null ? "--" : p) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}

class Game {
    Board board = new Board();
    String currentPlayer = "WHITE";

    public Game(){ initBoard(); }

    void initBoard() {
        // pawns
        for (int c=0;c<8;c++) {
            board.set(1,c,new Piece("PAWN","BLACK",1,c));
            board.set(6,c,new Piece("PAWN","WHITE",6,c));
        }
        // rooks
        board.set(0,0,new Piece("ROOK","BLACK",0,0));
        board.set(0,7,new Piece("ROOK","BLACK",0,7));
        board.set(7,0,new Piece("ROOK","WHITE",7,0));
        board.set(7,7,new Piece("ROOK","WHITE",7,7));
        // simple: add kings only
        board.set(0,4,new Piece("KING","BLACK",0,4));
        board.set(7,4,new Piece("KING","WHITE",7,4));
    }

    public void move(int fr,int fc,int tr,int tc) {
        Piece p = board.get(fr,fc);
        if (p==null) { System.out.println("No piece at source"); return; }
        if (!p.color.equals(currentPlayer)) { System.out.println("Not " + currentPlayer + "'s turn"); return; }
        board.set(fr,fc,null);
        p.move(tr,tc);
        board.set(tr,tc,p);
        switchTurn();
    }

    void switchTurn() {
        currentPlayer = currentPlayer.equals("WHITE") ? "BLACK" : "WHITE";
    }
}

// Demo
public class ChessGame {
    public static void main(String[] args) {
        Game g = new Game();
        g.board.printBoard();

        g.move(6,4,4,4); // White pawn e2 to e4
        g.board.printBoard();

        g.move(1,4,3,4); // Black pawn e7 to e5
        g.board.printBoard();

        g.move(7,4,6,4); // White king moves
        g.board.printBoard();
    }
}
