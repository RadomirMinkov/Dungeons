package bg.sofia.uni.fmi.mjt.dungeons.utility;

import bg.sofia.uni.fmi.mjt.dungeons.characters.Character;
import bg.sofia.uni.fmi.mjt.dungeons.characters.ClassType;
import bg.sofia.uni.fmi.mjt.dungeons.characters.Minion;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MapElementAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NotEnoughExperienceException;
import bg.sofia.uni.fmi.mjt.dungeons.items.ManaPotion;
import bg.sofia.uni.fmi.mjt.dungeons.maps.Board;
import bg.sofia.uni.fmi.mjt.dungeons.maps.GameBoard;
import bg.sofia.uni.fmi.mjt.dungeons.maps.MapElement;
import bg.sofia.uni.fmi.mjt.dungeons.maps.Position;
import bg.sofia.uni.fmi.mjt.dungeons.user.Credentials;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.COLUMNS;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.ROWS;

public class GsonWriteTest {
    public static void main(String[] args) {
        Gson gson = new Gson();
        ManaPotion potion = new ManaPotion();
        String json = gson.toJson(potion);
        System.out.println(json);
        ManaPotion copiedPotion = gson.fromJson(json, ManaPotion.class);
        System.out.println(copiedPotion.getPoints());
        System.out.println(copiedPotion.toString());

        Position position = new Position(2, 3);
        Character me = new Character("radomir", position);
        List<User> users = new ArrayList<>();
        Map<ClassType, Character> r = new HashMap<>();
        r.put(ClassType.WARRIOR, me);
        users.add(new User(new Credentials("rminkov", "Alienkiller832"), r));
        users.add(new User(new Credentials("nikola", "Alienkiller832"), r));
        users.add(new User(new Credentials("MyPrecious", "Alienkiller832"), r));

        Board gameBoard = new GameBoard(ROWS, COLUMNS);
        List<Minion> minions = new ArrayList<>();
        try {
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 0, 2);
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 0, 3);
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 0, 8);
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 1, 2);
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 2, 5);
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 2, 6);
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 1, 7);
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 1, 8);
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 1, 9);
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 3, 0);
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 3, 1);
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 3, 6);
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 4, 8);
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 4, 5);
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 4, 0);
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 4, 9);
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 4, 11);
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 5, 10);
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 5, 1);
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 5, 6);
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 5, 7);
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 5, 8);
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 6, 2);
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 6, 3);
            gameBoard.getBoard().addElement(MapElement.OBSTACLE, 6, 4);
            gameBoard.getBoard().addElement(MapElement.TREASURE, 6, 10);
            gameBoard.getBoard().addElement(MapElement.TREASURE, 0, 1);
            gameBoard.getBoard().addElement(MapElement.TREASURE, 2, 8);
            gameBoard.getBoard().addElement(MapElement.TREASURE, 4, 2);
            Minion m1 = new Minion(1, new Position(2, 2));
            Minion m2 = new Minion(2, new Position(6, 1));
            Minion m3 = new Minion(3, new Position(5, 3));
            Minion m4 = new Minion(4, new Position(4, 10));
            Minion m5 = new Minion(2, new Position(4, 4));
            Minion m6 = new Minion(1, new Position(4, 3));
            minions.add(m1);
            minions.add(m2);
            minions.add(m3);
            minions.add(m4);
            minions.add(m5);
            minions.add(m6);

        } catch (MapElementAlreadyExistsException | NotEnoughExperienceException e) {
            System.out.println(e.getMessage());
        }
        try (FileWriter writer = new FileWriter(Constants.JSON_GAME_BOARD)) {
            Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
            gson1.toJson(gameBoard, writer);
            System.out.println(Constants.JSON_GAME_BOARD);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter(Constants.JSON_MINION_INFORMATION)) {
            Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
            gson1.toJson(minions, writer);
            System.out.println(Constants.JSON_MINION_INFORMATION);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
