package bg.sofia.uni.fmi.mjt.dungeons.maps;

import bg.sofia.uni.fmi.mjt.dungeons.utility.Matrix;

public interface Map {

    Matrix getMap();

    MapElement getTileType(int row, int column);

    void changeTile(int row, int column, MapElement type);
}
