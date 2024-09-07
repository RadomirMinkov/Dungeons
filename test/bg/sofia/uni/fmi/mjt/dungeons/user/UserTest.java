package bg.sofia.uni.fmi.mjt.dungeons.user;

import bg.sofia.uni.fmi.mjt.dungeons.characters.Character;
import bg.sofia.uni.fmi.mjt.dungeons.characters.ClassType;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MapElementAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MapElementDoesNotExistException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NoSuchCharacterException;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.Mode;
import bg.sofia.uni.fmi.mjt.dungeons.maps.GameBoard;
import bg.sofia.uni.fmi.mjt.dungeons.maps.Position;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Matrix;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserTest {

    private User user;
    private Credentials credentials;
    private Map<ClassType, Character> characters;
    private ClassType mockType;
    private Character mockCharacter;
    private GameBoard mockGameBoard;
    private Matrix matrix;

    @BeforeEach
    public void setUp() {
        credentials = new Credentials("username", "password");
        characters = new HashMap<>();
        user = new User(credentials, characters);
        mockCharacter = mock(Character.class);
        mockGameBoard = mock(GameBoard.class);

        user.createCharacter(ClassType.WARRIOR);
        user.createCharacter(ClassType.WIZARD);
        matrix = mock(Matrix.class);
        when(mockGameBoard.getBoard()).thenReturn(matrix);
    }

    @Test
    void testGetCharacterCount() {
        int count = user.getCharacterCount(); // Assuming this returns the count of characters
        assertEquals(2, count, "User should have 2 characters");
    }

    @Test
    public void testGetUsername() {
        assertEquals("username", user.getUsername(), "Expected username to be 'username'");
    }

    @Test
    void testGetCharacters() {
        Map<ClassType, Character> characters = user.getCharacters();  // Assuming a map of characters
        assertEquals(2, characters.size(), "User should have 2 characters");
        assertTrue(characters.containsKey(ClassType.WARRIOR), "User should have a Warrior character");
        assertTrue(characters.containsKey(ClassType.WIZARD), "User should have a Mage character");
    }

    @Test
    void testChangeCharacterAlreadyActive() throws Exception {
        user.changeCharacter(ClassType.WARRIOR, mockGameBoard);
        Message message = user.changeCharacter(ClassType.WARRIOR, mockGameBoard);

        assertEquals("You are already using this character", message.message());
        assertEquals(Mode.NORMAL, message.mode());
    }
    @Test
    public void testGetPassword() {
        assertEquals("password", user.getPassword(), "Expected password to be 'password'");
    }

    @Test
    public void testUpdateUsername() {
        user.updateUsername("newUsername");
        assertEquals("newUsername", user.getUsername(), "Expected username to be updated to 'newUsername'");
    }

    @Test
    public void testUpdatePassword() {
        user.updatePassword("newPassword");
        assertEquals("newPassword", user.getPassword(), "Expected password to be updated to 'newPassword'");
    }

    @Test
    public void testCreateCharacterSuccess() {
        Message result = user.createCharacter(ClassType.WARRIOR);
        assertEquals("You already character of this class!", result.message(), "Expected message for character creation");
        assertTrue(characters.containsKey(ClassType.WARRIOR), "Character should be created");
    }

    @Test
    public void testCreateCharacterAlreadyExists() {
        characters.put(ClassType.WARRIOR, new Character("Warrior", new Position(1, 1)));
        Message result = user.createCharacter(ClassType.WARRIOR);
        assertEquals("You already character of this class!", result.message(), "Expected message for existing character");
    }

    @Test
    public void testDeleteCharacterSuccess() throws NoSuchCharacterException {
        characters.put(ClassType.WIZARD, new Character("Mage", new Position(1, 1)));
        Message result = user.deleteCharacter(ClassType.WIZARD);
        assertEquals("Character successfully deleted!", result.message(), "Expected deletion success message");
        assertFalse(characters.containsKey(ClassType.WIZARD), "Character should be deleted");
    }

    @Test
    public void testDeleteCharacterThrowsNoSuchCharacterException() {
        assertThrows(NoSuchCharacterException.class, () -> user.deleteCharacter(ClassType.ROGUE),
                "Expected NoSuchCharacterException for non-existing character");
    }

    @Test
    public void testChangeCharacterSuccess() throws MapElementDoesNotExistException, MapElementAlreadyExistsException {
        Character warrior = new Character("Warrior", new Position(1, 1));
        Message result = user.changeCharacter(ClassType.WARRIOR, mockGameBoard);

        assertEquals("Switched to the warrior class!", result.message(), "Expected switch character success message");
        assertEquals(ClassType.WARRIOR, user.getActiveCharacter(), "Active character should be updated");

        Message newResult = user.changeCharacter(ClassType.WIZARD, mockGameBoard);
        assertEquals("Switched to the warrior class!", result.message(), "Expected switch character success message");
        assertEquals(ClassType.WIZARD, user.getActiveCharacter(), "Active character should be updated");
    }

    @Test
    public void testgetActiveCharacter() throws MapElementDoesNotExistException, MapElementAlreadyExistsException {
        Message result = user.changeCharacter(ClassType.WARRIOR, mockGameBoard);

        Character activeCharacter = user.getCharacter(ClassType.WARRIOR);

        assertNotNull(activeCharacter, "Character should not be null");
        assertEquals("warrior", activeCharacter.getName(), "The active character should be the Warrior character");
    }

    @Test
    public void testChangeCharacterToSameClass() throws MapElementDoesNotExistException, MapElementAlreadyExistsException {

        Character warrior = new Character("Warrior", new Position(1, 1));
        characters.put(ClassType.WARRIOR, warrior);
        user.changeCharacter(ClassType.WARRIOR, mockGameBoard);

        Message result = user.changeCharacter(ClassType.WARRIOR, mockGameBoard);
        assertEquals("You are already using this character", result.message(), "Expected message for already active character");
    }

    @Test
    public void testChangeCharacterNonExistent() throws MapElementDoesNotExistException, MapElementAlreadyExistsException {

        Message result = user.changeCharacter(ClassType.ROGUE, mockGameBoard);
        assertEquals("You don't have a character of this class!", result.message(), "Expected message for non-existing character");
    }

    @Test
    public void testCompareToDifferentUsernames() {
        User anotherUser = new User(new Credentials("anotherUser", "password"), new HashMap<>());
        assertTrue(user.compareTo(anotherUser) > 0, "Expected user to be greater than anotherUser based on username");
    }

    @Test
    public void testCompareToSameUsername() {
        User sameUser = new User(new Credentials("username", "differentPassword"), new HashMap<>());
        assertEquals(0, user.compareTo(sameUser), "Expected users to be equal based on username");
    }
}