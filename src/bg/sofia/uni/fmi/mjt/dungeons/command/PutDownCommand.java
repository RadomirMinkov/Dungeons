package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MapElementDoesNotExistException;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.Mode;
import bg.sofia.uni.fmi.mjt.dungeons.maps.MapElement;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import java.nio.channels.SelectionKey;

public class PutDownCommand implements UserCommand {
    GameEngine gameEngine;
    SelectionKey key;

    public PutDownCommand(GameEngine gameEngine, SelectionKey key) {
        if (null == gameEngine) {
            throw new IllegalArgumentException("The parameters are null!");
        }
        this.gameEngine = gameEngine;
        this.key = key;
    }

    @Override
    public Message execute() {
        User user = (User) key.attachment();
        try {
            gameEngine.getGameBoard().removeElementFromTile(user
                            .getCharacter(user.getActiveCharacter()).getPosition().getRow(),
                            user.getCharacter(user.getActiveCharacter())
                                    .getPosition().getColumn(), MapElement.TREASURE);
            return new Message("You left the treasure behind and it is lost forever!", Mode.NORMAL, null);
        } catch (MapElementDoesNotExistException e) {
            return new Message(e.getMessage(), Mode.NORMAL, null);
        }
    }
}
