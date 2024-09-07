package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MapElementAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MapElementDoesNotExistException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.UnknownCommandException;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.Mode;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import java.nio.channels.SelectionKey;

public class MoveCommand implements UserCommand {
    private GameEngine gameEngine;
    private SelectionKey key;
    private String direction;

    public MoveCommand(GameEngine gameEngine, SelectionKey key, String direction) {
        if (gameEngine == null || direction == null || key == null) {
            throw new IllegalArgumentException("The given arguments cannot be null!");
        }
        this.direction = direction;
        this.gameEngine = gameEngine;
        this.key = key;
    }

    @Override
    public Message execute() {
        try {
            Message message = gameEngine.movePlayer((User) key.attachment(), direction);
            return new Message(message.message()  + System.lineSeparator() +
                    gameEngine.getGameBoard().boardAsString().toString(),
                    message.mode());
        } catch (UnknownCommandException | MapElementDoesNotExistException | MapElementAlreadyExistsException e) {
            return new Message(e.getMessage(), Mode.NORMAL);
        }
    }
}
