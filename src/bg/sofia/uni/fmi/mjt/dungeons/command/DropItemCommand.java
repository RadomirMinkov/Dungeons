package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Pickable;

import java.nio.channels.SelectionKey;

public class DropItemCommand implements UserCommand {
    GameEngine gameEngine;
    SelectionKey key;
    String word;
    public DropItemCommand(GameEngine gameEngine, SelectionKey key, String word) {
        if (null == gameEngine || null == key || word == null) {
            throw new IllegalArgumentException("The parameters shouldn't be null!");
        }
        this.gameEngine = gameEngine;
        this.key = key;
        this.word = word;
    }

    @Override
    public Message execute() {
        return null;
    }
}
