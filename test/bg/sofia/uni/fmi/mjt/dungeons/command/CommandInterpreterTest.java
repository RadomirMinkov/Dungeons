package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.command.interpreter.CommandInterpreter;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.UnknownCommandException;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.Mode;
import bg.sofia.uni.fmi.mjt.dungeons.maps.GameBoard;
import bg.sofia.uni.fmi.mjt.dungeons.user.Credentials;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.SelectionKey;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CommandInterpreterTest {

    private CommandInterpreter commandInterpreter;
    private GameEngine gameEngine;
    private SelectionKey key;
    private User mockUser;
    private GameBoard board;

    @BeforeEach
    public void setUp() throws Exception {
        gameEngine = mock(GameEngine.class);
        commandInterpreter = new CommandInterpreter(gameEngine);
        key = mock(SelectionKey.class);
        mockUser = mock(User.class);
        when(key.attachment()).thenReturn(mockUser);
        board = mock(GameBoard.class);
        when(gameEngine.getGameBoard()).thenReturn(board);
    }

    @Test
    public void testInterpretNormalShowMap() throws UnknownCommandException {
        Message message = new Message("map", Mode.NORMAL);

        UserCommand command = commandInterpreter.intepretate(message, key);

        assertEquals(ShowMapCommand.class, command.getClass(), "Expected ShowMapCommand to be returned");
    }

    @Test
    public void testInterpretNormalMoveCommand() throws UnknownCommandException {
        Message message = new Message("move north", Mode.NORMAL);

        UserCommand command = commandInterpreter.intepretate(message, key);

        assertEquals(MoveCommand.class, command.getClass(), "Expected MoveCommand to be returned");
    }

    /*"""
                map
                help
                login <username> <password>
                logout
                inventory
                move <direction>
                create character <class>
                delete character <class>
                choose/change character <class>
                create user <username> <password>
                delete user <username> <password>
                exit"""; */
    @Test
    public void testInterpretNormalLoginCommand() throws UnknownCommandException {
        Message message = new Message("login username password", Mode.NORMAL);

        UserCommand command = commandInterpreter.intepretate(message, key);

        assertEquals(LoginCommand.class, command.getClass(), "Expected LoginCommand to be returned");
    }

    @Test
    public void testInterpretNormalLogoutCommand() throws UnknownCommandException {
        Message message = new Message("logout", Mode.NORMAL);

        UserCommand command = commandInterpreter.intepretate(message, key);

        assertEquals(LogoutCommand.class, command.getClass(), "Expected LogoutCommand to be returned");
    }

    @Test
    public void testInterpretNormalInventoryCommand() throws UnknownCommandException {
        Message message = new Message("inventory", Mode.NORMAL);

        UserCommand command = commandInterpreter.intepretate(message, key);

        assertEquals(SeeInventoryCommand.class, command.getClass(), "Expected SeeInventoryCommand to be returned");
    }

    @Test
    public void testInterpretNormalCreateCharacterCommand() throws UnknownCommandException {
        Message message = new Message("create character warrior", Mode.NORMAL);

        UserCommand command = commandInterpreter.intepretate(message, key);

        assertEquals(CreateCharacter.class, command.getClass(), "Expected CreateCharacter to be returned");
    }

    @Test
    public void testInterpretNormalDeleteCharacter() throws UnknownCommandException {
        Message message = new Message("delete character warrior", Mode.NORMAL);
        UserCommand command = commandInterpreter.intepretate(message, key);

        assertEquals(DeleteCharacter.class, command.getClass(), "Expected DeleteCharacter to be returned");
    }

    @Test
    public void testInterpretNormalCreateUserCommand() throws UnknownCommandException {
        Message message = new Message("create user username password", Mode.NORMAL);

        UserCommand command = commandInterpreter.intepretate(message, key);

        assertEquals(CreateUserCommand.class, command.getClass(), "Expected CreateUserCommand to be returned");
    }

    @Test
    public void testInterpretNormalDeleteUserCommand() throws UnknownCommandException {
        Message message = new Message("delete user username password", Mode.NORMAL);

        UserCommand command = commandInterpreter.intepretate(message, key);

        assertEquals(DeleteUserCommand.class, command.getClass(), "Expected DeleteUserCommand to be returned");
    }

    @Test
    public void testInterpretNormalChangeCharacterCommand() throws UnknownCommandException {
        Message message = new Message("change character warrior", Mode.NORMAL);

        UserCommand command = commandInterpreter.intepretate(message, key);

        assertEquals(ChangeCharacterCommand.class, command.getClass(), "Expected ChangeCharacterCommand to be returned");
    }

    @Test
    public void testInterpretNormalInvalidCommand() {
        Message message1 = new Message("invalidcommand", Mode.NORMAL);
        Message message3 = new Message("user t a d", Mode.NORMAL);
        Message message4 = new Message("r t a", Mode.NORMAL);
        Message message0 = new Message("", Mode.NORMAL);

        assertThrows(UnknownCommandException.class, () -> commandInterpreter.intepretate(message1, key),
                "Expected UnknownCommandException to be thrown");
        assertThrows(UnknownCommandException.class, () -> commandInterpreter.intepretate(message0, key),
                "Expected UnknownCommandException to be thrown");
        assertThrows(UnknownCommandException.class, () -> commandInterpreter.intepretate(message3, key),
                "Expected UnknownCommandException to be thrown");
        assertThrows(UnknownCommandException.class, () -> commandInterpreter.intepretate(message4, key),
                "Expected UnknownCommandException to be thrown");
    }

    @Test
    public void testInterpretTradeInvalidCommand() {
        Message message1 = new Message("invalidcommand", Mode.TRADE);
        Message message4 = new Message("r t ", Mode.TRADE);
        Message message0 = new Message("", Mode.TRADE);

        assertThrows(UnknownCommandException.class, () -> commandInterpreter.intepretate(message1, key),
                "Expected UnknownCommandException to be thrown");
        assertThrows(UnknownCommandException.class, () -> commandInterpreter.intepretate(message0, key),
                "Expected UnknownCommandException to be thrown");
        assertThrows(UnknownCommandException.class, () -> commandInterpreter.intepretate(message4, key),
                "Expected UnknownCommandException to be thrown");
    }

    @Test
    public void testInterpretTreasureInvalidCommand() {
        Message message1 = new Message("invalidcommand", Mode.TREASURE);
        Message message0 = new Message("", Mode.TREASURE);

        assertThrows(UnknownCommandException.class, () -> commandInterpreter.intepretate(message1, key),
                "Expected UnknownCommandException to be thrown");
        assertThrows(UnknownCommandException.class, () -> commandInterpreter.intepretate(message0, key),
                "Expected UnknownCommandException to be thrown");
    }

    @Test
    public void testInterpretChooseInvalidCommand() {
        Message message1 = new Message("invalidcommand", Mode.CHOOSE);
        Message message2 = new Message("invalidcommand wa", Mode.CHOOSE);
        Message message0 = new Message("", Mode.CHOOSE);

        assertThrows(UnknownCommandException.class, () -> commandInterpreter.intepretate(message1, key),
                "Expected UnknownCommandException to be thrown");
        assertThrows(UnknownCommandException.class, () -> commandInterpreter.intepretate(message0, key),
                "Expected UnknownCommandException to be thrown");
        assertThrows(UnknownCommandException.class, () -> commandInterpreter.intepretate(message2, key),
                "Expected UnknownCommandException to be thrown");
    }

    @Test
    public void testInterpretNormalEmptyStringCommand() {
        Message message = new Message("", Mode.NORMAL);

        assertThrows(UnknownCommandException.class, () -> commandInterpreter.intepretate(message, key),
                "Expected UnknownCommandException to be thrown");
    }

    @Test
    public void testInterpretBattleAttack() throws UnknownCommandException {
        Message message = new Message("attack", Mode.BATTLE);

        UserCommand command = commandInterpreter.intepretate(message, key);

        assertEquals(AttackCommand.class, command.getClass(), "Expected AttackCommand to be returned");
    }

    @Test
    public void testInterpretPowerUpAttack() throws UnknownCommandException {
        Message message = new Message("power up", Mode.BATTLE);

        UserCommand command = commandInterpreter.intepretate(message, key);

        assertEquals(PowerUpCommand.class, command.getClass(), "Expected PowerUpCommand to be returned");
    }

    @Test
    public void testInterpretUsePotionAttack() throws UnknownCommandException {
        Message message = new Message("use potion item", Mode.BATTLE);

        UserCommand command = commandInterpreter.intepretate(message, key);

        assertEquals(UsePotionCommand.class, command.getClass(), "Expected UsePotionCommand to be returned");
    }

    @Test
    public void testInterpretBattleDefend() throws UnknownCommandException {
        Message message = new Message("defend", Mode.BATTLE);

        UserCommand command = commandInterpreter.intepretate(message, key);

        assertEquals(DefendCommand.class, command.getClass(), "Expected DefendCommand to be returned");
    }

    @Test
    public void testInterpretBattleInvalidCommand() {
        Message message = new Message("invalidbattle", Mode.BATTLE);
        Message message0 = new Message("", Mode.BATTLE);
        Message message2 = new Message("invalidbattle 2", Mode.BATTLE);

        assertThrows(UnknownCommandException.class, () -> commandInterpreter.intepretate(message, key),
                "Expected UnknownCommandException to be thrown");
        assertThrows(UnknownCommandException.class, () -> commandInterpreter.intepretate(message0, key),
                "Expected UnknownCommandException to be thrown");
        assertThrows(UnknownCommandException.class, () -> commandInterpreter.intepretate(message2, key),
                "Expected UnknownCommandException to be thrown");
    }

    @Test
    public void testInterpretTreasurePickUp() throws UnknownCommandException {
        Message message = new Message("pick up", Mode.TREASURE);

        UserCommand command = commandInterpreter.intepretate(message, key);

        assertEquals(PickUpCommand.class, command.getClass(), "Expected PickUpCommand to be returned");
    }

    @Test
    public void testInterpretTreasurePutDown() throws UnknownCommandException {
        Message message = new Message("put down", Mode.TREASURE);

        UserCommand command = commandInterpreter.intepretate(message, key);

        assertEquals(PutDownCommand.class, command.getClass(), "Expected PutDownCommand to be returned");
    }

    @Test
    public void testInterpretTreasureDropItem() throws UnknownCommandException {
        Message message = new Message("drop item", Mode.TREASURE);

        UserCommand command = commandInterpreter.intepretate(message, key);

        assertEquals(DropItemCommand.class, command.getClass(), "Expected DropItemCommand to be returned");
    }

    @Test
    public void testInterpretChooseTrade() throws UnknownCommandException {
        Message message = new Message("trade", Mode.CHOOSE);

        UserCommand command = commandInterpreter.intepretate(message, key);

        assertEquals(TradeCommand.class, command.getClass(), "Expected TradeCommand to be returned");
    }

    @Test
    public void testInterpretChooseFight() throws UnknownCommandException {
        Message message = new Message("fight", Mode.CHOOSE);

        UserCommand command = commandInterpreter.intepretate(message, key);

        assertEquals(FightCommand.class, command.getClass(), "Expected FightCommand to be returned");
    }

    @Test
    public void testInterpretChooseNothingTrade() throws UnknownCommandException {
        Message message = new Message("nothing", Mode.CHOOSE);

        UserCommand command = commandInterpreter.intepretate(message, key);

        assertEquals(DoNothingCommand.class, command.getClass(), "Expected DoNothingCommand to be returned");
    }


    /*@Test
    public void testExecuteValidCommand() {
        Message message = new Message("map", Mode.NORMAL);
        ShowMapCommand mockCommand = mock(ShowMapCommand.class);

        when(mockCommand.execute()).thenReturn(new Message("Map shown", Mode.NORMAL));

        try {
            when(commandInterpreter.intepretate(message, key)).thenReturn(mockCommand);
        } catch (UnknownCommandException e) {
            throw new RuntimeException(e);
        }

        Message result = commandInterpreter.executeCommand(message, key);

        assertEquals("Map shown", result.message(), "Expected 'Map shown' message.");
    }
*/
    @Test
    public void testInterpretTradeOffer() throws UnknownCommandException {
        Message message = new Message("offer item", Mode.TRADE);

        UserCommand command = commandInterpreter.intepretate(message, key);

        assertEquals(OfferCommand.class, command.getClass(), "Expected OfferCommand to be returned");
    }

    @Test
    public void testInterpretTradeAccept() throws UnknownCommandException {
        Message message = new Message("accept item", Mode.TRADE);

        UserCommand command = commandInterpreter.intepretate(message, key);

        assertEquals(AcceptItemCommand.class, command.getClass(), "Expected AcceptItemCommand to be returned");
    }

    @Test
    public void testInterpretInvalid() throws UnknownCommandException {
        Message message = new Message("invalidTRADE", Mode.TRADE);

        assertThrows(UnknownCommandException.class, () -> commandInterpreter.intepretate(message, key),
                "Expected UnknownCommandException to be thrown");
    }

    @Test
    public void testExecuteUnknownCommand() {
        Message message = new Message("invalidcommand", Mode.NORMAL);
        Message result = commandInterpreter.executeCommand(message, key);

        assertEquals("Unknown command!", result.message(), "Expected UnknownCommandException message");
    }
}