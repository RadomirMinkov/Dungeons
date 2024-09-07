package bg.sofia.uni.fmi.mjt.dungeons.gamelogic;

import bg.sofia.uni.fmi.mjt.dungeons.characters.Character;
import bg.sofia.uni.fmi.mjt.dungeons.characters.ClassType;
import bg.sofia.uni.fmi.mjt.dungeons.characters.Minion;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.EmptyInventoryException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.EnoughMinionsExistException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.FullBackPackException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.ItemNotFoundException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MapElementAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MapElementDoesNotExistException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MinionDiedException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NoSuchCharacterException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NotEnoughExperienceException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.ThereIsNoSuchUserException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.UnknownCommandException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.UserIsNotLoggedInException;
import bg.sofia.uni.fmi.mjt.dungeons.items.HealthPotion;
import bg.sofia.uni.fmi.mjt.dungeons.items.ManaPotion;
import bg.sofia.uni.fmi.mjt.dungeons.items.Spell;
import bg.sofia.uni.fmi.mjt.dungeons.items.Treasure;
import bg.sofia.uni.fmi.mjt.dungeons.items.Weapon;
import bg.sofia.uni.fmi.mjt.dungeons.maps.Board;
import bg.sofia.uni.fmi.mjt.dungeons.maps.MapElement;
import bg.sofia.uni.fmi.mjt.dungeons.maps.Position;
import bg.sofia.uni.fmi.mjt.dungeons.user.Credentials;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.JsonReader;
import bg.sofia.uni.fmi.mjt.dungeons.utility.JsonWriter;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.COLUMNS;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.EMPTY_STRING;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.FOUR;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.MAX_MINIONS_NUMBER;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.ONE;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.ROWS;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.SPELL_NAMES;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.THREE;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.TWO;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.WEAPONS_NAMES;

public class GameEngine {

    private int currentMinionsNumber;
    List<User> allRegisteredUsers;
    Map<String, String> usersCredentials;
    Map<String, String> activeCredentials;

    List<SelectionKey> keys;
    List<User> activeUsers;
    List<Minion> minions;
    Board gameBoard;
    JsonReader jsonReader;
    JsonWriter jsonWriter;

    private static final Random RANDOM = new Random();
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

    public int activeMinions() {
        return currentMinionsNumber;
    }

    public Message deleteUser(String username, String password, SelectionKey key) throws ThereIsNoSuchUserException {
        if (usersCredentials.get(username) == null) {
            throw new ThereIsNoSuchUserException("No such user exist!");
        }
        if (!usersCredentials.get(username).equals(password)) {
            return new Message("Wrong password!", Mode.NORMAL, null);
        }
        User user = (User) key.attachment();
        if (!user.getPassword().equals(password) || !user.getUsername().equals(username)) {
            throw new IllegalArgumentException("Can't delete user that you are not logged into!");
        }
        activeUsers.remove(user);
        allRegisteredUsers.remove(user);
        key.attach(null);
        keys.remove(key);
        return new Message("Successful deleting of a user!", Mode.NORMAL, null);
    }

    public Message createUser(String username, String password, SelectionKey key) {
        if (usersCredentials.get(username) != null) {
            return new Message("User with that username already exists!", Mode.NORMAL, null);
        }
        User newUser = new User(new Credentials(username, password), new HashMap<>());
        allRegisteredUsers.add(newUser);
        usersCredentials.put(username, password);
        return new Message("Successful creation of a user! Hope that you will be having a great time!"
                + login(username, password, key).message(), Mode.NORMAL, null);
    }

    public Message login(String username, String password, SelectionKey key) {
        if (usersCredentials.get(username) == null) {
            return new Message("There is no such registered user!", Mode.NORMAL, null);
        }
        if (!usersCredentials.get(username).equals(password)) {
            return new Message("Password is wrong! Try again!", Mode.NORMAL, null);
        }
        if (activeCredentials.get(username) != null) {
            return new Message("This user is already logged in!", Mode.NORMAL, null);
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
        return new Message("Successful login into your account", Mode.NORMAL, null);
    }

    public Message logout(SelectionKey key) throws UserIsNotLoggedInException, MapElementDoesNotExistException {
        User user = (User) key.attachment();
        assertUserIsLogged(user);
        activeUsers.remove(user);
        key.attach(null);
        loggedPlayers--;
        clearMap(user);
        return new Message("You logged out successfully!", Mode.NORMAL, null);
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
            return new Message("Not possible to move cause you will go out of the map!", Mode.NORMAL, null);
        }
        if (gameBoard.getTile(row, column).contains(MapElement.OBSTACLE)) {
            return new Message("There is wall that prevents your movement!", Mode.NORMAL, null);
        }
        if (gameBoard.getTile(row, column).stream().filter(element -> element.equals(MapElement.PLAYER)).count() >= 2) {
            return new Message("There is already enough players on that square!", Mode.NORMAL, null);
        }
        gameBoard.removeElementFromTile(user.getCharacter(user.getActiveCharacter()).getPosition().getRow(),
                user.getCharacter(user.getActiveCharacter()).getPosition().getColumn(), MapElement.PLAYER);

        user.getCharacter(user.getActiveCharacter()).setPosition(new Position(row, column));
        gameBoard.addElementToTile(row, column, MapElement.PLAYER);
        Message message = inspectTile(row, column);
        return new Message("You moved " + direction + "! " + message.message(), message.mode(), null);
    }

    public Message inspectTile(int row, int column) {
        PriorityQueue<MapElement> tile = gameBoard.getTile(row, column);
        if ((TWO == tile.size() || ONE == tile.size()) && tile.contains(MapElement.FREE_SPACE)) {
            return new Message(EMPTY_STRING, Mode.NORMAL, null);
        }
        if (tile.contains(MapElement.MINION)) {
            return new Message("Starting battle with a minion of the evil!", Mode.BATTLE, null);
        }
        long players = tile.stream()
                .filter(element -> element.equals(MapElement.PLAYER))
                .count();
        if (players > 1) {
            return new Message("Initiate trade or battle with player!", Mode.CHOOSE, null);
        }
        if (tile.contains(MapElement.TREASURE)) {
            return new Message("You stumbled upon a treasure! Do you want to pick it?", Mode.TREASURE, null);
        }
        return new Message(EMPTY_STRING, Mode.NORMAL, null);
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
        currentMinionsNumber++;
    }

    private void populateBoardWithMinions() throws MapElementAlreadyExistsException {
        for (Minion minion : minions) {
            gameBoard.getBoard().addElement(MapElement.MINION, minion.getPosition().getRow(),
                    minion.getPosition().getColumn());
            currentMinionsNumber++;
        }
    }

    private void clearMap(User user) throws MapElementDoesNotExistException {
        gameBoard.removeElementFromTile(user.getCharacter(user.getActiveCharacter()).getPosition().getRow(),
                user.getCharacter(user.getActiveCharacter()).getPosition().getColumn(),
                MapElement.PLAYER);

    }

    public Message pickUpTreasure(User user) {
        Treasure treasure = generateTreasure();
        try {
            user.getCharacter(user.getActiveCharacter()).pickUpItem(treasure);
            gameBoard.removeElementFromTile(user.getCharacter(user.getActiveCharacter()).getPosition().getRow(),
                    user.getCharacter(user.getActiveCharacter()).getPosition().getColumn(), MapElement.TREASURE);
            return new Message("Successfully picked up the treasure", Mode.NORMAL, null);
        } catch (FullBackPackException e) {
            return new Message(e.getMessage(), Mode.TRADE, null);
        } catch (MapElementDoesNotExistException e) {
            return new Message(e.getMessage(), Mode.NORMAL, null);
        }
    }

    private Treasure generateTreasure() {
        int randomNumber = RANDOM.nextInt(FOUR) + 1;
        int level = RANDOM.nextInt(FOUR);
        return switch (randomNumber) {
            case ONE -> new HealthPotion();
            case TWO -> new ManaPotion();
            case THREE -> new Spell(generateSpellName(), level);
            case FOUR -> new Weapon(generateWeaponName(), level);
            default -> throw new IllegalStateException("Unexpected value: " + randomNumber);
        };
    }

    private String generateWeaponName() {
        return WEAPONS_NAMES.get(RANDOM.nextInt(WEAPONS_NAMES.size()));

    }

    private String generateSpellName() {
        return SPELL_NAMES.get(RANDOM.nextInt(SPELL_NAMES.size()));
    }

    public Message acceptItem(User user, String item)
            throws EmptyInventoryException, ItemNotFoundException, FullBackPackException {
        Position position = user.getCharacter(user.getActiveCharacter()).getPosition();
        User offeringUser = null;
        for (User users : activeUsers) {
            if (!users.equals(user) && users.getCharacter(users.getActiveCharacter()).getPosition().equals(position)) {
                offeringUser = users;
            }
        }
        if (offeringUser == null) {
            return new Message("There is no other user on this tile!", Mode.NORMAL, null);
        }
        List<Treasure> items = offeringUser
                .getCharacter(offeringUser.getActiveCharacter()).getInventory().getElements();
        for (Treasure treasure : items) {
            if (treasure.getName().equals(item) && treasure.getIsOffered()) {
                offeringUser.getCharacter(offeringUser.getActiveCharacter()).getInventory().removeElement(treasure);
                treasure.setOffered();
                user.getCharacter(user.getActiveCharacter()).getInventory().addElement(treasure);
                return new Message("The trade was completed!", Mode.CHOOSE, null);
            }
        }
        return new Message("There was no offered item with this name!", Mode.CHOOSE, null);
    }

    public Message offerItem(User user, String item) {
        Position position = user.getCharacter(user.getActiveCharacter()).getPosition();
        User sending = null;
        for (User users : activeUsers) {
            if (!users.equals(user) && users.getCharacter(users.getActiveCharacter()).getPosition().equals(position)) {
                sending = users;
            }
        }
        if (sending == null) {
            return new Message("There is no other user on this tile!", Mode.NORMAL, null);
        }
        List<Treasure> items = user
                .getCharacter(user.getActiveCharacter()).getInventory().getElements();
        for (Treasure treasure : items) {
            if (treasure.getName().equals(item) && treasure.getIsOffered()) {
                treasure.setOffered();
                return new Message("The trade was completed!", Mode.CHOOSE, null);
            }
        }
        return new Message("There was no offered item with this name!", Mode.CHOOSE, null);
    }

    public Message attack(User player, User user)
            throws MapElementDoesNotExistException, NotEnoughExperienceException,
            EnoughMinionsExistException, MinionDiedException {
        Character playerCharacter = player.getCharacter(player.getActiveCharacter());
        if (null == user) {
            for (Minion minion : minions) {
                if (minion.getPosition().getRow() == playerCharacter.getPosition().getRow() &&
                        minion.getPosition().getColumn() == playerCharacter.getPosition().getColumn()) {
                    try {
                        return playerCharacter.attack(playerCharacter.getWeapon(), minion);
                    } catch (MinionDiedException e) {
                        gameBoard.removeElementFromTile(minion.getPosition().getRow(),
                                minion.getPosition().getColumn(), MapElement.MINION);

                        spawnMinion(RANDOM.nextInt(FOUR));
                        return new Message(e.getMessage(), Mode.NORMAL, null);
                    }

                }
            }
        } else {
            for (User activeUser : activeUsers) {
                Character otherCharacter = activeUser.getCharacter(activeUser.getActiveCharacter());
                if (otherCharacter.getPosition().getRow() == playerCharacter.getPosition().getRow() &&
                        otherCharacter.getPosition().getColumn() == playerCharacter.getPosition().getColumn()) {
                    return playerCharacter.attack(playerCharacter.getWeapon(), otherCharacter);

                }
            }
        }
        return new Message("Unsuccessful attack", Mode.BATTLE, user);
    }
}
