package bg.sofia.uni.fmi.mjt.dungeons.utility;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MapElementAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MapElementDoesNotExistException;
import bg.sofia.uni.fmi.mjt.dungeons.maps.MapElement;

import java.util.PriorityQueue;

public class Matrix {
    private PriorityQueue<MapElement>[][] matrix;
    int rows;
    int columns;

    public Matrix(int rows, int columns) {
        matrix = new PriorityQueue[rows][columns];
        this.rows = rows;
        this.columns = columns;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = new PriorityQueue<>();
                matrix[i][j].add(MapElement.FREE_SPACE);
            }
        }
    }

    public PriorityQueue<MapElement> getElement(int row, int column) {
        return matrix[row][column];
    }

    public boolean containsElementType(int row, int column, MapElement type) {
        return matrix[row][column].contains(type);
    }

    public void addElement(MapElement element, int row, int column)
            throws MapElementAlreadyExistsException {
        if (matrix[row][column].contains(element) && element != MapElement.PLAYER) {
            throw new MapElementAlreadyExistsException("This element already exists on this tile!");
        }
        matrix[row][column].add(element);
    }

    public void removeElement(MapElement element, int row, int column) throws MapElementDoesNotExistException {
        if (!matrix[row][column].contains(element)) {
            throw new MapElementDoesNotExistException("This element doesn't exists on this tile!");
        }
        matrix[row][column].remove(element);
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
}
