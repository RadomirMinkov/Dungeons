package bg.sofia.uni.fmi.mjt.dungeons.maps;

import bg.sofia.uni.fmi.mjt.dungeons.utility.Matrix;

public class GameMap implements Map {

    private Matrix map;

    public GameMap(int rows, int columns) {
        map = new Matrix(rows, columns);
    }

    @Override
    public Matrix getMap() {
        return map;
    }

    @Override
    public MapElement getTileType(int row, int column) {
        return map.getElement(row, column);
    }

    @Override
    public void changeTile(int row, int column, MapElement type) {
        map.setElement(type, row, column);
    }
}
