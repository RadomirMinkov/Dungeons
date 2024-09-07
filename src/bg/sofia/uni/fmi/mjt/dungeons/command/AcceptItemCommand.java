package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.EmptyInventoryException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.FullBackPackException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.ItemNotFoundException;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.Mode;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import java.nio.channels.SelectionKey;

public class AcceptItemCommand implements UserCommand {
    private GameEngine gameEngine;
    private SelectionKey key;
    private String item;

    public AcceptItemCommand(GameEngine gameEngine, SelectionKey key, String item) {
        if (null == gameEngine || null == key || null == item) {
            throw new IllegalArgumentException("The parameters should no be null!");
        }
        this.gameEngine = gameEngine;
        this.key = key;
        this.item = item;
    }

    @Override
    public Message execute() {
        User user = (User) key.attachment();
        try {
            return gameEngine.acceptItem(user, item);
        } catch (EmptyInventoryException | ItemNotFoundException | FullBackPackException e) {
            return new Message(e.getMessage(), Mode.CHOOSE, null);
        }
    }
}
