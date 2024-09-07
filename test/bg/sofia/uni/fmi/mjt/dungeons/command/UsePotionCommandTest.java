package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.characters.Character;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.EmptyInventoryException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.ItemNotFoundException;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.Mode;
import bg.sofia.uni.fmi.mjt.dungeons.items.ManaPotion;
import bg.sofia.uni.fmi.mjt.dungeons.items.Potion;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.SelectionKey;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsePotionCommandTest {

    private SelectionKey keyMock;
    private User userMock;
    private UsePotionCommand command;
    private String potionType;

    @BeforeEach
    void setUp() {
        keyMock = mock(SelectionKey.class);
        userMock = mock(User.class);

        when(keyMock.attachment()).thenReturn(userMock);

        potionType = "manaPotion";
        command = new UsePotionCommand(keyMock, potionType);
    }

    @Test
    void testConstructorThrowsIllegalArgumentExceptionWhenNullParams() {
        assertThrows(IllegalArgumentException.class, () -> new UsePotionCommand(null, potionType),
                "Constructor should throw IllegalArgumentException when SelectionKey is null");

        assertThrows(IllegalArgumentException.class, () -> new UsePotionCommand(keyMock, null),
                "Constructor should throw IllegalArgumentException when potionType is null");
    }

    @Test
    void testExecuteReturnsCorrectMessageOnValidManaPotion() throws EmptyInventoryException, ItemNotFoundException {

        Character characterMock = mock(Character.class);
        when(userMock.getCharacter(userMock.getActiveCharacter())).thenReturn(characterMock);

        Message result = command.execute();

        verify(characterMock).usePotion(any(ManaPotion.class));

        assertEquals("You used the potion successfully!", result.message(),
                "The message should indicate successful potion usage");
        assertEquals(Mode.BATTLE, result.mode(), "Mode should be BATTLE after successful potion use");
    }

    @Test
    void testExecuteReturnsMessageForInvalidPotionType() {
        command = new UsePotionCommand(keyMock, "invalidPotionType");
        Message result = command.execute();

        assertEquals("No such potion exists", result.message(),
                "The message should indicate that the potion type does not exist");
        assertEquals(Mode.BATTLE, result.mode(), "Mode should be BATTLE when no such potion exists");
    }

    @Test
    void testExecuteHandlesEmptyInventoryException() throws EmptyInventoryException, ItemNotFoundException {
        Character characterMock = mock(Character.class);
        when(userMock.getCharacter(userMock.getActiveCharacter())).thenReturn(characterMock);
        doThrow(new EmptyInventoryException("Inventory is empty")).when(characterMock).usePotion(any(Potion.class));

        Message result = command.execute();

        assertEquals("Inventory is empty", result.message(),
                "The message should contain the exception message");
        assertEquals(Mode.BATTLE, result.mode(), "Mode should be BATTLE when an exception occurs");
    }

    @Test
    void testExecuteHandlesItemNotFoundException() throws EmptyInventoryException, ItemNotFoundException {
        Character characterMock = mock(Character.class);
        when(userMock.getCharacter(userMock.getActiveCharacter())).thenReturn(characterMock);
        doThrow(new ItemNotFoundException("Item not found")).when(characterMock).usePotion(any(Potion.class));

        Message result = command.execute();

        assertEquals("Item not found", result.message(),
                "The message should contain the exception message");
        assertEquals(Mode.BATTLE, result.mode(), "Mode should be BATTLE when an exception occurs");
    }
}