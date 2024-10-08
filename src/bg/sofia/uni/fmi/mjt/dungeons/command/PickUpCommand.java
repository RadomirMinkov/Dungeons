package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import java.nio.channels.SelectionKey;

public class PickUpCommand implements UserCommand {
    private GameEngine gameEngine;
    private SelectionKey key;

    public PickUpCommand(GameEngine gameEngine, SelectionKey key) {
        if (null == gameEngine || null == key) {
            throw new IllegalArgumentException("The parameters should no be null!");
        }
        this.gameEngine = gameEngine;
        this.key = key;
    }

    @Override
    public Message execute() {
        return gameEngine.pickUpTreasure((User) key.attachment());
    }
}
