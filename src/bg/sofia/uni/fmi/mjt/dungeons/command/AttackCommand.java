package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.Mode;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import java.nio.channels.SelectionKey;

public class AttackCommand implements UserCommand {
    private GameEngine gameEngine;
    private SelectionKey key;
    private User user;

    public AttackCommand(GameEngine gameEngine, SelectionKey key, User user) {
        if (null == gameEngine || null == key) {
            throw new IllegalArgumentException("The parameters should no be null!");
        }
        this.gameEngine = gameEngine;
        this.key = key;
        this.user = user;
    }

    @Override
    public Message execute() {
        try {
            User player = (User) key.attachment();
            return gameEngine.attack(player, user);
        } catch (Exception e) {
            return new Message(e.getMessage(), Mode.NORMAL, null);
        }
    }
}
