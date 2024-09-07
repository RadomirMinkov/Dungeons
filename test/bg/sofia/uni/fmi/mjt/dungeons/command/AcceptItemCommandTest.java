package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.EmptyInventoryException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.FullBackPackException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.ItemNotFoundException;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.Mode;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.SelectionKey;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AcceptItemCommandTest {

    private GameEngine gameEngineMock;
    private SelectionKey keyMock;
    private User userMock;
    private AcceptItemCommand command;
    private String item;

    @BeforeEach
    void setUp() {
        gameEngineMock = mock(GameEngine.class);
        keyMock = mock(SelectionKey.class);
        userMock = mock(User.class);
        item = "Sword";

        when(keyMock.attachment()).thenReturn(userMock);

        command = new AcceptItemCommand(gameEngineMock, keyMock, item);
    }

    @Test
    void testConstructorThrowsIllegalArgumentExceptionWhenNullParams() {
        assertThrows(IllegalArgumentException.class, () -> new AcceptItemCommand(null, keyMock, item),
                "Constructor should throw IllegalArgumentException when GameEngine is null");

        assertThrows(IllegalArgumentException.class, () -> new AcceptItemCommand(gameEngineMock, null, item),
                "Constructor should throw IllegalArgumentException when SelectionKey is null");

        assertThrows(IllegalArgumentException.class, () -> new AcceptItemCommand(gameEngineMock, keyMock, null),
                "Constructor should throw IllegalArgumentException when item is null");
    }

    @Test
    void testExecuteReturnsCorrectMessageOnSuccess()
            throws EmptyInventoryException, ItemNotFoundException, FullBackPackException {
        Message expectedMessage = new Message("Item accepted", Mode.BATTLE, null);

        when(gameEngineMock.acceptItem(userMock, item)).thenReturn(expectedMessage);

        Message result = command.execute();

        assertEquals(expectedMessage, result, "The message returned should match the expected message from gameEngine.acceptItem()");
        verify(gameEngineMock).acceptItem(userMock, item);
    }

    @Test
    void testExecuteHandlesEmptyInventoryException()
            throws EmptyInventoryException, ItemNotFoundException, FullBackPackException {

        when(gameEngineMock.acceptItem(userMock, item)).thenThrow(new EmptyInventoryException("Inventory is empty"));

        Message result = command.execute();

        assertEquals("Inventory is empty", result.message(), "The message should contain the exception message");
        assertEquals(Mode.CHOOSE, result.mode(), "Mode should be CHOOSE after exception");
    }

    @Test
    void testExecuteHandlesItemNotFoundException()
            throws EmptyInventoryException, ItemNotFoundException, FullBackPackException {

        when(gameEngineMock.acceptItem(userMock, item)).thenThrow(new ItemNotFoundException("Item not found"));

        Message result = command.execute();

        assertEquals("Item not found", result.message(), "The message should contain the exception message");
        assertEquals(Mode.CHOOSE, result.mode(), "Mode should be CHOOSE after exception");
    }

    @Test
    void testExecuteHandlesFullBackPackException()
            throws EmptyInventoryException, ItemNotFoundException, FullBackPackException {

        when(gameEngineMock.acceptItem(userMock, item)).thenThrow(new FullBackPackException("Backpack is full"));

        Message result = command.execute();

        assertEquals("Backpack is full", result.message(), "The message should contain the exception message");
        assertEquals(Mode.CHOOSE, result.mode(), "Mode should be CHOOSE after exception");
    }
}