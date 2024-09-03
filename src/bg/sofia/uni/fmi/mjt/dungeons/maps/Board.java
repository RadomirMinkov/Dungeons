package bg.sofia.uni.fmi.mjt.dungeons.maps;

import bg.sofia.uni.fmi.mjt.dungeons.utility.Matrix;

public interface Board {

    public Matrix getBoard();

    public MapElement getTileType(int row, int column);

    public void changeTile(int row, int column, MapElement type);

    public void printBoard();

    public StringBuilder boardAsString();
}
