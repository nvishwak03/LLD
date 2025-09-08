//Cell- row, col, hasShip, hit, (constructor only)
// Ship : id, name, size, hits, isSunk(), registerHit()
// Board: 10x10 cells, list of ships, placeShip(), receiveShot(), allSunk(), printBoard()
// Game: board, shoot(position), initShips(), print()

import java.util.*;

// Cell
class Cell {
    int row, col;
    boolean hasShip;
    boolean hit;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.hasShip = false;
        this.hit = false;
    }
}

// Ship
class Ship {
    String name;
    int size;
    int hits;

    public Ship(String name, int size) {
        this.name = name;
        this.size = size;
        this.hits = 0;
    }

    public boolean isSunk() {
        return hits >= size;
    }

    public void registerHit() {
        hits++;
    }
}

// Board
class Board {
    Cell[][] grid = new Cell[5][5]; // smaller 5x5 for simplicity
    List<Ship> ships = new ArrayList<>();

    public Board() {
        for (int r = 0; r < 5; r++)
            for (int c = 0; c < 5; c++)
                grid[r][c] = new Cell(r, c);
    }

    public void placeShip(String name, int size, int row, int col) {
        Ship s = new Ship(name, size);
        ships.add(s);
        for (int i = 0; i < size; i++) {
            grid[row][col + i].hasShip = true; // place horizontally
        }
    }

    public String receiveShot(int row, int col) {
        Cell cell = grid[row][col];
        if (cell.hit) return "Already shot!";
        cell.hit = true;
        if (cell.hasShip) {
            // just count against first ship for simplicity
            ships.get(0).registerHit();
            if (ships.get(0).isSunk()) return "HIT and SUNK!";
            return "HIT!";
        } else {
            return "MISS!";
        }
    }

    public void printBoard() {
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                Cell cell = grid[r][c];
                if (!cell.hit) System.out.print(". ");
                else if (cell.hasShip) System.out.print("X ");
                else System.out.print("o ");
            }
            System.out.println();
        }
        System.out.println();
    }
}

// Game
public class BattleshipDemo {
    public static void main(String[] args) {
        Board board = new Board();
        board.placeShip("Destroyer", 2, 2, 1); // simple hardcoded placement

        board.printBoard();

        System.out.println(board.receiveShot(2, 1)); // should be HIT
        System.out.println(board.receiveShot(2, 2)); // should be HIT and SUNK
        System.out.println(board.receiveShot(0, 0)); // should be MISS

        board.printBoard();
    }
}
