package bg.sofia.uni.fmi.mjt.dungeons.gamelogic;

import bg.sofia.uni.fmi.mjt.dungeons.characters.ClassType;
import bg.sofia.uni.fmi.mjt.dungeons.characters.Minion;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.EnoughMinionsExistException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MapElementAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MapElementDoesNotExistException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NoSuchCharacterException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NotEnoughExperienceException;
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

import java.nio.channels.SelectionKey;
import java.util.*;

import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.*;

public class GameEngine {

    private static int currentMinionsNumber;
    List<User> allRegisteredUsers;
    Map<String, String> usersCredentials;
    Map<String, String> activeCredentials;

    List<SelectionKey> keys;
    List<User> activeUsers;
    List<Minion> minions;
    Board gameBoard;
    JsonReader jsonReader;
    JsonWriter jsonWriter;

    public static int loggedPlayers;

    static {
        loggedPlayers = 0;
    }

    {
        this.jsonReader = new JsonReader();
        this.jsonWriter = new JsonWriter();
        this.usersCredentials = new HashMap<>();
        this.activeCredentials = new HashMap<>();
        this.keys = new ArrayList<>();
    }

    public GameEngine() throws MapElementAlreadyExistsException {
        activeUsers = new ArrayList<>();
        this.gameBoard = jsonReader.readGameBoardFromJson();
        this.allRegisteredUsers = jsonReader.readUsersFromJson(usersCredentials);
        this.minions = jsonReader.readMinionsFromJson();
        populateBoardWithMinions();
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
        keys.remove(key);
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
        if (activeCredentials.get(username) != null) {
            return new Message("This user is already logged in!");
        }
        for (User user : allRegisteredUsers) {
            if (user.getUsername().equals(username)) {
                activeUsers.add(user);
                activeCredentials.put(username, password);
                loggedPlayers++;
                key.attach(user);
                keys.add(key);
            }
        }
        return new Message("Successful login into your account");
    }

    public Message logout(SelectionKey key) throws UserIsNotLoggedInException {
        User user = (User) key.attachment();
        assertUserIsLogged(user);
        activeUsers.remove(user);
        key.attach(null);
        loggedPlayers--;
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

    public Message changeCharacter(User user, ClassType classType, Board gameBoard)
            throws MapElementAlreadyExistsException, MapElementDoesNotExistException {
        return user.changeCharacter(classType, gameBoard);
    }

    public Message createCharacter(User user, ClassType classType) {
        return user.createCharacter(classType);
    }

    public Message movePlayer(User user, String direction)
            throws UnknownCommandException, MapElementDoesNotExistException, MapElementAlreadyExistsException {
        int column = user.getCharacter(user.getActiveCharacter()).getPosition().getColumn();
        int row = user.getCharacter(user.getActiveCharacter()).getPosition().getRow();
        switch (direction) {
            case "up" -> row -= 1;
            case "down" -> row += 1;
            case "right" -> column += 1;
            case "left" -> column -= 1;
            default -> throw new UnknownCommandException("Unknown command!");
        }
        if (column < 0 || column >= COLUMNS && row < 0 || row >= ROWS) {
            return new Message("Not possible to move cause you will go out of the map!");
        }
        if (gameBoard.getTile(row, column).contains(MapElement.OBSTACLE)) {
            return new Message("There is wall that prevents your movement!");
        }
        gameBoard.removeElementFromTile(user.getCharacter(user.getActiveCharacter()).getPosition().getRow(),
                user.getCharacter(user.getActiveCharacter()).getPosition().getColumn(), MapElement.PLAYER);

        user.getCharacter(user.getActiveCharacter()).setPosition(new Position(row, column))
        ;
        gameBoard.addElementToTile(row, column, MapElement.PLAYER);
        return new Message("You moved " + direction + "! " + inspectTile(row, column));
    }

    private String inspectTile(int row, int column) {
        PriorityQueue<MapElement> tile = gameBoard.getTile(row, column);
        if (TWO == tile.size() || ONE == tile.size()) {
            return EMPTY_STRING;
        }
        if (tile.contains(MapElement.MINION)) {
            return "Starting battle with a minion of the evil!";
        }
        long players = tile.stream()
                .filter(element -> element.equals(MapElement.PLAYER))
                .count();
        if (players > 1) {
            return "Initiate trade or battle with player!";
        }
        if (tile.contains(MapElement.TREASURE)) {
            return "You stumbled upon a treasure! Do you want to pick it?";
        }
        return EMPTY_STRING;
    }

    private boolean availableTile(int row, int column) {
        return !(gameBoard.getTile(row, column).contains(MapElement.OBSTACLE) ||
                gameBoard.getTile(row, column).contains(MapElement.PLAYER) ||
                gameBoard.getTile(row, column).contains(MapElement.MINION));
    }

    public Position genPosition() {
        Random random = new Random();
        boolean successful = false;
        int row = 0;
        int column = 0;
        while (!successful) {
            row = random.nextInt(ROWS);
            column = random.nextInt(COLUMNS);

            if (availableTile(row, column)) {
                successful = true;
            }
        }
        return new Position(row, column);
    }

    public void spawnMinion(int level) throws EnoughMinionsExistException, NotEnoughExperienceException {
        if (currentMinionsNumber >= MAX_MINIONS_NUMBER) {
            throw new EnoughMinionsExistException("There are already enough minions on the map!");
        }
        Position minionPosition = genPosition();
        gameBoard.getTile(minionPosition.getRow(), minionPosition.getColumn()).add(MapElement.MINION);
    }

    private void populateBoardWithMinions() throws MapElementAlreadyExistsException {
        for (Minion minion : minions) {
            gameBoard.getBoard().addElement(MapElement.MINION, minion.getPosition().getRow(),
                    minion.getPosition().getColumn());
            currentMinionsNumber++;
        }
    }
}
