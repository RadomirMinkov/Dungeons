package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.characters.Character;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.EmptyInventoryException;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.Mode;
import bg.sofia.uni.fmi.mjt.dungeons.items.BackPack;
import bg.sofia.uni.fmi.mjt.dungeons.items.Treasure;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DropItemCommandTest {

    private GameEngine gameEngineMock;
    private SelectionKey keyMock;
    private User userMock;
    private DropItemCommand command;
    private String itemName;

    @BeforeEach
    void setUp() {
        gameEngineMock = mock(GameEngine.class);
        keyMock = mock(SelectionKey.class);
        userMock = mock(User.class);

        when(keyMock.attachment()).thenReturn(userMock);

        itemName = "Sword";
        command = new DropItemCommand(gameEngineMock, keyMock, itemName);
    }

    @Test
    void testConstructorThrowsIllegalArgumentExceptionWhenNullParams() {
        assertThrows(IllegalArgumentException.class, () -> new DropItemCommand(null, keyMock, itemName),
                "Constructor should throw IllegalArgumentException when gameEngine is null");

        assertThrows(IllegalArgumentException.class, () -> new DropItemCommand(gameEngineMock, null, itemName),
                "Constructor should throw IllegalArgumentException when key is null");

        assertThrows(IllegalArgumentException.class, () -> new DropItemCommand(gameEngineMock, keyMock, null),
                "Constructor should throw IllegalArgumentException when word is null");
    }

    @Test
    void testExecuteDropsItemSuccessfully() throws EmptyInventoryException {
        List<Treasure> treasureList = new ArrayList<>();
        Treasure treasureMock = mock(Treasure.class);
        when(treasureMock.getName()).thenReturn(itemName);
        treasureList.add(treasureMock);

        Character characterMock = mock(Character.class);
        BackPack inventoryMock = mock(BackPack.class);
        when(characterMock.getInventory()).thenReturn(inventoryMock);
        when(userMock.getCharacter(userMock.getActiveCharacter())).thenReturn(characterMock);
        when(inventoryMock.getElements()).thenReturn(treasureList);

        Message result = command.execute();

        verify(inventoryMock).removeElement(0);

        assertEquals("You dropped Swordsuccessfully and you can pick up the reasure!", result.message(),
                "The message should indicate the item was successfully dropped");
        assertEquals(Mode.TREASURE, result.mode(), "Mode should be TREASURE after item is dropped");
    }

    @Test
    void testExecuteHandlesEmptyInventoryException() throws EmptyInventoryException {
        List<Treasure> treasureList = new ArrayList<>();
        Treasure treasureMock = mock(Treasure.class);
        when(treasureMock.getName()).thenReturn(itemName);
        treasureList.add(treasureMock);

        Character characterMock = mock(Character.class);
        BackPack inventoryMock = mock(BackPack.class);
        when(characterMock.getInventory()).thenReturn(inventoryMock);
        when(userMock.getCharacter(userMock.getActiveCharacter())).thenReturn(characterMock);
        when(inventoryMock.getElements()).thenReturn(treasureList);

        doThrow(new EmptyInventoryException("Inventory is empty")).when(inventoryMock).removeElement(anyInt());

        Message result = command.execute();

        assertEquals("Inventory is emptyYou can pick the treasure!", result.message(),
                "The message should include the exception message when inventory is empty");
        assertEquals(Mode.TREASURE, result.mode(), "Mode should be TREASURE when an exception occurs");
    }

    @Test
    void testExecuteItemNotInInventory() throws EmptyInventoryException {
        List<Treasure> treasureList = new ArrayList<>();
        Treasure treasureMock = mock(Treasure.class);
        when(treasureMock.getName()).thenReturn("DifferentItem");
        treasureList.add(treasureMock);

        Character characterMock = mock(Character.class);
        BackPack inventoryMock = mock(BackPack.class);
        when(characterMock.getInventory()).thenReturn(inventoryMock);
        when(userMock.getCharacter(userMock.getActiveCharacter())).thenReturn(characterMock);
        when(inventoryMock.getElements()).thenReturn(treasureList);

        Message result = command.execute();

        verify(inventoryMock, never()).removeElement(anyInt());

        assertNotEquals("You dropped Swordsuccessfully and you can pick up the reasure!", result.message(),
                "The message should not indicate success when item is not found in inventory");
    }
}