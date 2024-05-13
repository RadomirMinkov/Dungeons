package bg.sofia.uni.fmi.mjt.dungeons.utility;

import bg.sofia.uni.fmi.mjt.dungeons.maps.MapElement;

public class Matrix {
    private MapElement[][] matrix;

    public Matrix(int rows, int columns) {
        matrix = new MapElement[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = MapElement.FREE_SPACE;
            }
        }
    }

    public MapElement getElement(int row, int column) {
        return matrix[row][column];
    }

    public void setElement(MapElement element, int row, int column) {
        matrix[row][column] = element;
    }
}
