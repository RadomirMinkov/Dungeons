package bg.sofia.uni.fmi.mjt.dungeons.gamelogic;

import bg.sofia.uni.fmi.mjt.dungeons.characters.Character;
import bg.sofia.uni.fmi.mjt.dungeons.characters.ClassType;
import bg.sofia.uni.fmi.mjt.dungeons.characters.Minion;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.*;
import bg.sofia.uni.fmi.mjt.dungeons.items.Treasure;
import bg.sofia.uni.fmi.mjt.dungeons.maps.Board;
import bg.sofia.uni.fmi.mjt.dungeons.maps.MapElement;
import bg.sofia.uni.fmi.mjt.dungeons.maps.Position;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.JsonReader;
import bg.sofia.uni.fmi.mjt.dungeons.utility.JsonWriter;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameEngineTest {

    private GameEngine gameEngine;
    private JsonReader mockJsonReader;
    private JsonWriter mockJsonWriter;
    private User mockUser;
    private Board mockBoard;
    private Character mockCharacter;
    private SelectionKey mockKey;
    private Position mockPosition;
    private ClassType mockType;

    @BeforeEach
    void setUp() throws MapElementAlreadyExistsException {
        mockJsonReader = mock(JsonReader.class);
        mockJsonWriter = mock(JsonWriter.class);
        mockUser = mock(User.class);
        mockBoard = mock(Board.class);
        mockKey = mock(SelectionKey.class);
        mockCharacter = mock(Character.class);
        mockPosition = mock(Position.class);
        mockType = mock(ClassType.class);

        List<User> users = new ArrayList<>();
        List<Minion> minions = new ArrayList<>();
        when(mockJsonReader.readGameBoardFromJson()).thenReturn(mockBoard);
        when(mockJsonReader.readUsersFromJson(anyMap())).thenReturn(users);
        when(mockJsonReader.readMinionsFromJson()).thenReturn(minions);
        when(mockCharacter.getPosition()).thenReturn(mockPosition);

        gameEngine = new GameEngine();
        gameEngine.jsonReader = mockJsonReader;
        gameEngine.jsonWriter = mockJsonWriter;
    }

    @Test
    void testCreateUserSuccess() {
        String username = "testUser";
        String password = "testPass";
        when(mockKey.attachment()).thenReturn(mockUser);

        Message result = gameEngine.createUser(username, password, mockKey);

        assertEquals("Successful creation of a user! Hope that you will be having a great time!Successful login into your account", result.message());
        assertEquals(Mode.NORMAL, result.mode());
    }

    @Test
    void testCreateUserAlreadyExists() {
        String username = "testUser";
        String password = "testPass";
        gameEngine.usersCredentials.put(username, password);

        Message result = gameEngine.createUser(username, password, mockKey);

        assertEquals("User with that username already exists!", result.message());
        assertEquals(Mode.NORMAL, result.mode());
    }

    @Test
    void testLoginSuccess() {
        when(mockKey.attachment()).thenReturn(mockUser);

        Message result = gameEngine.login("rminkov", "alana", mockKey);

        assertEquals("Successful login into your account", result.message());
        assertEquals(Mode.NORMAL, result.mode());
        assertEquals(mockUser, mockKey.attachment());
    }

    @Test
    void testLoginWrongPassword() {
        String username = "testUser";
        String password = "testPass";
        gameEngine.usersCredentials.put(username, "wrongPassword");

        Message result = gameEngine.login(username, password, mockKey);

        assertEquals("Password is wrong! Try again!", result.message());
        assertEquals(Mode.NORMAL, result.mode());
    }

    @Test
    void testLoginNoSuchUser() {
        String username = "nonExistentUser";
        String password = "testPass";

        Message result = gameEngine.login(username, password, mockKey);

        assertEquals("There is no such registered user!", result.message());
        assertEquals(Mode.NORMAL, result.mode());
    }

    @Test
    void testDeleteUserSuccess() throws ThereIsNoSuchUserException {
        String username = "testUser";
        String password = "testPass";
        when(mockUser.getUsername()).thenReturn(username);
        when(mockUser.getPassword()).thenReturn(password);
        gameEngine.usersCredentials.put(username, password);
        gameEngine.activeUsers.add(mockUser);
        when(mockKey.attachment()).thenReturn(mockUser);

        Message result = gameEngine.deleteUser(username, password, mockKey);

        assertEquals("Successful deleting of a user!", result.message());
        assertEquals(Mode.NORMAL, result.mode());
        assertFalse(gameEngine.activeUsers.contains(mockUser));
    }

    @Test
    void testDeleteUserWrongPassword() throws ThereIsNoSuchUserException {
        String username = "testUser";
        String password = "testPass";
        gameEngine.usersCredentials.put(username, "wrongPassword");

        Message result = gameEngine.deleteUser(username, password, mockKey);

        assertEquals("Wrong password!", result.message());
        assertEquals(Mode.NORMAL, result.mode());
    }

    @Test
    void testDeleteUserNoSuchUser() {
        String username = "nonExistentUser";
        String password = "testPass";

        assertThrows(ThereIsNoSuchUserException.class, () -> gameEngine.deleteUser(username, password, mockKey));
    }

    @Test
    void testLogoutSuccess() throws UserIsNotLoggedInException, MapElementDoesNotExistException, MapElementAlreadyExistsException {
        when(mockKey.attachment()).thenReturn(mockUser);
        when(mockUser.getCharacter(any())).thenReturn(mock(Character.class));
        when(mockUser.getCharacter(any()).getPosition()).thenReturn(mockPosition);
        when(mockPosition.getRow()).thenReturn(2);
        when(mockPosition.getColumn()).thenReturn(4);
        gameEngine.activeUsers.add(mockUser);

        gameEngine.getGameBoard().addElementToTile(2,4,MapElement.PLAYER);
        Message result = gameEngine.logout(mockKey);

        assertEquals("You logged out successfully!", result.message());
        assertEquals(Mode.NORMAL, result.mode());
        assertFalse(gameEngine.activeUsers.contains(mockUser));
    }

    @Test
    void testLogoutUserNotLoggedIn() {
        when(mockKey.attachment()).thenReturn(null);

        assertThrows(UserIsNotLoggedInException.class, () -> gameEngine.logout(mockKey));
    }

    @Test
    void testPickUpTreasureSuccess() throws MapElementAlreadyExistsException {
        Treasure mockTreasure = mock(Treasure.class);
        when(mockTreasure.toString()).thenReturn("Health Potion");
        when(mockUser.getCharacter(any())).thenReturn(mockCharacter);
        when(mockCharacter.getPosition()).thenReturn(new Position(0,0));
        gameEngine.getGameBoard().addElementToTile(0,0 , MapElement.TREASURE);
        Message result = gameEngine.pickUpTreasure(mockUser);

        assertEquals("Successfully picked up the treasure", result.message());
        assertEquals(Mode.NORMAL, result.mode());
    }

    @Test
    void testSpawnMinionSuccess() throws EnoughMinionsExistException, NotEnoughExperienceException {

        gameEngine.spawnMinion(1);

        assertEquals(7, gameEngine.activeMinions());
    }
    @Test
    void testGenPosition() {
        Position position = gameEngine.genPosition();
        assertTrue(position.getRow() >= 0 && position.getRow() < ROWS);
        assertTrue(position.getColumn() >= 0 && position.getColumn() < COLUMNS);
    }

    @Test
    void testMovePlayerUpSuccess() throws UnknownCommandException, MapElementDoesNotExistException, MapElementAlreadyExistsException {
        Position mockPosition = mock(Position.class);
        when(mockPosition.getRow()).thenReturn(2);
        when(mockPosition.getColumn()).thenReturn(4);
        when(mockUser.getCharacter(any())).thenReturn(mock(Character.class));
        when(mockUser.getCharacter(any()).getPosition()).thenReturn(mockPosition);
        gameEngine.getGameBoard().getBoard().addElement(MapElement.PLAYER, 2, 4);
        PriorityQueue<MapElement> mockTile = new PriorityQueue<>();
        when(mockBoard.getTile(1, 4)).thenReturn(mockTile);

        Message result = gameEngine.movePlayer(mockUser, "up");

        assertEquals("You moved up", result.message().substring(0, 12));
    }

    @Test
    void testMovePlayerUnknownCommandThrowsException() {
        when(mockUser.getCharacter(any())).thenReturn(mock(Character.class));
        when(mockUser.getCharacter(any()).getPosition()).thenReturn(mockPosition);
        when(mockPosition.getRow()).thenReturn(5);
        when(mockPosition.getColumn()).thenReturn(5);
        assertThrows(UnknownCommandException.class, () -> gameEngine.movePlayer(mockUser, "invalid"));
    }

    @Test
    void testMovePlayerObstaclePreventsMovement() throws UnknownCommandException, MapElementDoesNotExistException, MapElementAlreadyExistsException {
        Position mockPosition = mock(Position.class);
        when(mockPosition.getRow()).thenReturn(5);
        when(mockPosition.getColumn()).thenReturn(5);
        when(mockUser.getCharacter(any())).thenReturn(mock(Character.class));
        when(mockUser.getCharacter(any()).getPosition()).thenReturn(mockPosition);

        PriorityQueue<MapElement> mockTile = new PriorityQueue<>();
        mockTile.add(MapElement.OBSTACLE);
        when(mockBoard.getTile(4, 5)).thenReturn(mockTile);

        Message result = gameEngine.movePlayer(mockUser, "up");

        assertEquals("There is wall that prevents your movement!", result.message());
    }

    @Test
    void testCreateCharacterSuccess() {
        Message expectedMessage = new Message("Character created", Mode.NORMAL);
        when(mockUser.createCharacter(mockType)).thenReturn(expectedMessage);

        Message result = gameEngine.createCharacter(mockUser, mockType);

        assertEquals(expectedMessage, result);
        verify(mockUser).createCharacter(mockType);
    }

    @Test
    void testChangeCharacterSuccess() throws MapElementAlreadyExistsException, MapElementDoesNotExistException {
        Board mockBoard = mock(Board.class);
        Message expectedMessage = new Message("Character changed", Mode.NORMAL);
        when(mockUser.changeCharacter(mockType, mockBoard)).thenReturn(expectedMessage);

        Message result = gameEngine.changeCharacter(mockUser, mockType, mockBoard);

        assertEquals(expectedMessage, result);
        verify(mockUser).changeCharacter(mockType, mockBoard);
    }

    @Test
    void testChangeCharacterThrowsMapElementAlreadyExistsException() throws MapElementAlreadyExistsException, MapElementDoesNotExistException {
        Board mockBoard = mock(Board.class);
        when(mockUser.changeCharacter(mockType, mockBoard)).thenThrow(new MapElementAlreadyExistsException("Element already exists"));

        assertThrows(MapElementAlreadyExistsException.class, () -> gameEngine.changeCharacter(mockUser, mockType, mockBoard));
        verify(mockUser).changeCharacter(mockType, mockBoard);
    }

    @Test
    void testChangeCharacterThrowsMapElementDoesNotExistException() throws MapElementAlreadyExistsException, MapElementDoesNotExistException {
        Board mockBoard = mock(Board.class);
        when(mockUser.changeCharacter(mockType, mockBoard)).thenThrow(new MapElementDoesNotExistException("Element does not exist"));

        assertThrows(MapElementDoesNotExistException.class, () -> gameEngine.changeCharacter(mockUser, mockType, mockBoard));
        verify(mockUser).changeCharacter(mockType, mockBoard);
    }

    @Test
    void testDeleteCharacterSuccess() throws NoSuchCharacterException {
        Message expectedMessage = new Message("Character deleted", Mode.NORMAL);
        when(mockUser.deleteCharacter(mockType)).thenReturn(expectedMessage);

        Message result = gameEngine.deleteCharacter(mockUser, mockType);

        assertEquals(expectedMessage, result);
        verify(mockUser).deleteCharacter(mockType);
    }

    @Test
    void testDeleteCharacterThrowsNoSuchCharacterException() throws NoSuchCharacterException {
        when(mockUser.deleteCharacter(mockType)).thenThrow(new NoSuchCharacterException("Character not found"));

        assertThrows(NoSuchCharacterException.class, () -> gameEngine.deleteCharacter(mockUser, mockType));
        verify(mockUser).deleteCharacter(mockType);
    }

    @Test
    public void testInspectTileWithOneOrTwoElements() {
        PriorityQueue<MapElement> tile = new PriorityQueue<>();

        when(mockBoard.getTile(0, 0)).thenReturn(tile);

        Message result = gameEngine.inspectTile(0, 0);
        assertEquals("", result.message());
        assertEquals(Mode.NORMAL, result.mode());

        tile.add(MapElement.OBSTACLE);
        result = gameEngine.inspectTile(0, 0);
        assertEquals("", result.message());
        assertEquals(Mode.NORMAL, result.mode());
    }

    @Test
    public void testInspectTileWithMinion() throws MapElementAlreadyExistsException {
        PriorityQueue<MapElement> tile = new PriorityQueue<>();
        tile.add(MapElement.MINION);

        when(mockBoard.getTile(0, 0)).thenReturn(tile);
        gameEngine.getGameBoard().addElementToTile(0,0,MapElement.MINION);
        gameEngine.getGameBoard().addElementToTile(0,0,MapElement.PLAYER);
        Message result = gameEngine.inspectTile(0, 0);
        assertEquals("Starting battle with a minion of the evil!", result.message());
        assertEquals(Mode.BATTLE, result.mode());
    }

    @Test
    public void testInspectTileWithMultiplePlayers() throws MapElementAlreadyExistsException {
        PriorityQueue<MapElement> tile = new PriorityQueue<>();
        tile.add(MapElement.PLAYER);
        tile.add(MapElement.PLAYER);

        gameEngine.getGameBoard().addElementToTile(0,0,MapElement.PLAYER);
        gameEngine.getGameBoard().addElementToTile(0,0,MapElement.PLAYER);
        Message result = gameEngine.inspectTile(0, 0);
        assertEquals("Initiate trade or battle with player!", result.message());
        assertEquals(Mode.CHOOSE, result.mode());
    }

    @Test
    public void testInspectTileWithTreasure() throws MapElementAlreadyExistsException {

        gameEngine.getGameBoard().addElementToTile(0,0, MapElement.TREASURE);
        gameEngine.getGameBoard().addElementToTile(0,0, MapElement.PLAYER);
        Message result = gameEngine.inspectTile(0, 0);
        assertEquals("You stumbled upon a treasure! Do you want to pick it?", result.message());
        assertEquals(Mode.TREASURE, result.mode());
    }

    @Test
    public void testInspectTileWithNoSpecificConditions() {
        PriorityQueue<MapElement> tile = new PriorityQueue<>();

        when(mockBoard.getTile(0, 0)).thenReturn(tile);

        Message result = gameEngine.inspectTile(0, 0);
        assertEquals("", result.message());
        assertEquals(Mode.NORMAL, result.mode());
    }
}
