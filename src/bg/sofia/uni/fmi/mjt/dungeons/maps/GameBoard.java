package bg.sofia.uni.fmi.mjt.dungeons.maps;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MapElementAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MapElementDoesNotExistException;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Matrix;

import java.util.PriorityQueue;

public class GameBoard implements Board {

    private Matrix board;

    public GameBoard(int rows, int columns) {

        board = new Matrix(rows, columns);
    }

    public GameBoard(Matrix board) {
        this.board = board;
    }

    @Override
    public Matrix getBoard() {
        return board;
    }

    @Override
    public PriorityQueue<MapElement> getTile(int row, int column) {
        return board.getElement(row, column);
    }

    @Override
    public void addElementToTile(int row, int column, MapElement type) throws MapElementAlreadyExistsException {
        board.addElement(type, row, column);
    }

    @Override
    public void removeElementFromTile(int row, int column, MapElement type) throws MapElementDoesNotExistException {
        board.removeElement(type, row, column);
    }

    @Override
    public void printBoard() {
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                System.out.print(board.getElement(i, j).peek() + " ");
            }
            System.out.println();
        }
    }

    @Override
    public StringBuilder boardAsString() {
        StringBuilder boardString = new StringBuilder();
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                boardString.append(board.getElement(i, j).peek()).append(" ");
            }
            boardString.append(System.getProperty("line.separator"));
        }
        return boardString;
    }
}
