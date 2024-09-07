package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import java.nio.channels.SelectionKey;

public class OfferCommand implements UserCommand {
    private GameEngine gameEngine;
    private SelectionKey key;
    private String item;

    public OfferCommand(GameEngine gameEngine, SelectionKey key, String item) {
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
        gameEngine.offerItem(user, item);
        return null;
    }
}
