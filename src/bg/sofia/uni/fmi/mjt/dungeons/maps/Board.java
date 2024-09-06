package bg.sofia.uni.fmi.mjt.dungeons.maps;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MapElementAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MapElementDoesNotExistException;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Matrix;

import java.util.PriorityQueue;

public interface Board {

    public Matrix getBoard();

    public PriorityQueue<MapElement> getTile(int row, int column);

    public void addElementToTile(int row, int column, MapElement type) throws MapElementAlreadyExistsException;

    public void removeElementFromTile(int row, int column, MapElement type) throws MapElementDoesNotExistException;

    public void printBoard();

    public StringBuilder boardAsString();

}
