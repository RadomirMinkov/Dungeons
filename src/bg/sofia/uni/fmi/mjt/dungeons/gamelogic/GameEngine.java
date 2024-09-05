package bg.sofia.uni.fmi.mjt.dungeons.gamelogic;

import bg.sofia.uni.fmi.mjt.dungeons.characters.ClassType;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NoSuchCharacterException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.ThereIsNoSuchUserException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.UnknownCommandException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.UserIsNotLoggedInException;
import bg.sofia.uni.fmi.mjt.dungeons.maps.Board;
import bg.sofia.uni.fmi.mjt.dungeons.maps.MapElement;
import bg.sofia.uni.fmi.mjt.dungeons.maps.Position;
import bg.sofia.uni.fmi.mjt.dungeons.user.Credentials;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.JsonReader;
import bg.sofia.uni.fmi.mjt.dungeons.utility.JsonWriter;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import javax.naming.OperationNotSupportedException;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.COLUMNS;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.ROWS;

public class GameEngine {

    List<User> allRegisteredUsers;
    Map<String, String> usersCredentials;
    List<User> activeUsers;
    Board gameBoard;
    JsonReader jsonReader;
    JsonWriter jsonWriter;

    {
        this.jsonReader = new JsonReader();
        this.jsonWriter = new JsonWriter();
        this.usersCredentials = new HashMap<>();
    }

    public GameEngine() {
        activeUsers = new ArrayList<>();
        this.gameBoard = jsonReader.readGameBoardFromJson();
        this.allRegisteredUsers = jsonReader.readUsersFromJson(usersCredentials);
    }

    public Board getGameBoard() {
        return gameBoard;
    }

    public Message deleteUser(String username, String password, SelectionKey key) throws ThereIsNoSuchUserException {
        if (usersCredentials.get(username) == null) {
            throw new ThereIsNoSuchUserException("No such user exist!");
        }
        if (!usersCredentials.get(username).equals(password)) {
            return new Message("Wrong password!");
        }
        User user = (User) key.attachment();
        if (!user.getPassword().equals(password) || !user.getUsername().equals(username)) {
            throw new IllegalArgumentException("Can't delete user that you are not logged into!");
        }
        activeUsers.remove(user);
        allRegisteredUsers.remove(user);
        key.attach(null);
        return new Message("Successful deleting of a user!");
    }

    public Message createUser(String username, String password, SelectionKey key) {
        if (usersCredentials.get(username) != null) {
            return new Message("User with that username already exists!");
        }
        User newUser = new User(new Credentials(username, password), new HashMap<>());
        allRegisteredUsers.add(newUser);
        usersCredentials.put(username, password);
        return new Message("Successful creation of a user! Hope that you will be having a great time!"
                + login(username, password, key).message());
    }

    public Message login(String username, String password, SelectionKey key) {
        if (usersCredentials.get(username) == null) {
            return new Message("There is no such registered user!");
        }
        if (!usersCredentials.get(username).equals(password)) {
            return new Message("Password is wrong! Try again!");
        }
        for (User user : allRegisteredUsers) {
            if (user.getUsername().equals(username)) {
                activeUsers.add(user);
                key.attach(user);
            }
        }
        return new Message("Successful login into your account");
    }

    public Message logout(SelectionKey key) throws UserIsNotLoggedInException {
        User user = (User) key.attachment();
        assertUserIsLogged(user);
        activeUsers.remove(user);
        key.attach(null);
        return new Message("You logged out successfully!");
    }

    public void assertUserIsLogged(User user) throws UserIsNotLoggedInException {
        if (null == user || !activeUsers.contains(user)) {
            throw new UserIsNotLoggedInException("You are not logged in your account!");
        }
    }

    public Message deleteCharacter(User user, ClassType classType) throws NoSuchCharacterException {
        return user.deleteCharacter(classType);
    }

    public Message changeCharacter(User user, ClassType classType, Board gameBoard) {
        return user.changeCharacter(classType, gameBoard);
    }

    public Message createCharacter(User user, ClassType classType) {
        return user.createCharacter(classType);
    }

    public Message movePlayer(User user, ClassType type, String direction) throws UnknownCommandException {
        int column = user.getCharacter(type).getPosition().getColumn();
        int row = user.getCharacter(type).getPosition().getRow();
        switch (direction) {
            case "up" -> column += 1;
            case "down" -> column -= 1;
            case "right" -> row += 1;
            case "left" -> row -= 1;
            default -> throw new UnknownCommandException("Unknown command!");
        }
        if (column < 0 || column >= COLUMNS && row < 0 || row >= ROWS) {
            return new Message("Not possible to move cause you will go out of the map!");
        }
        if (gameBoard.getTileType(row, column).equals(MapElement.OBSTACLE)) {
            return new Message("There is wall that prevents your movement!");
        }
        user.getCharacter(type).setPosition(new Position(row, column));
    }
}
