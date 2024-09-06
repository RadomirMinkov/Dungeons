package bg.sofia.uni.fmi.mjt.dungeons.utility;

import bg.sofia.uni.fmi.mjt.dungeons.maps.Board;
import bg.sofia.uni.fmi.mjt.dungeons.maps.GameBoard;
import com.google.gson.Gson;

import java.io.FileReader;

import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.COLUMNS;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.ROWS;

public class GSonReadTest {
    public static void main(String... args) {

        Gson gson = new Gson();
        try (FileReader reader = new FileReader(Constants.JSON_GAME_BOARD)) {
            Board gameBoard = gson.fromJson(reader, GameBoard.class);

            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLUMNS; j++) {
                    System.out.print(gameBoard.getTile(i, j).peek() + " ");
                }
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
