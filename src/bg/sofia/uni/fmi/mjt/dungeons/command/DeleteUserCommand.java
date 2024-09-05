package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.ThereIsNoSuchUserException;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import java.nio.channels.SelectionKey;

public class DeleteUserCommand implements UserCommand {
    private final GameEngine gameEngine;
    private final String username;
    private final String password;
    private SelectionKey key;

    public DeleteUserCommand(GameEngine gameEngine, String username, String password, SelectionKey key) {
        if (gameEngine == null || username == null || password == null || key == null) {
            throw new IllegalArgumentException("The given arguments can't be null!");
        }
        this.gameEngine = gameEngine;
        this.username = username;
        this.password = password;
        this.key = key;
    }

    @Override
    public Message execute() {
        try {
            return gameEngine.deleteUser(username, password, key);
        } catch (ThereIsNoSuchUserException e) {
            return new Message(e.getMessage());
        }
    }
}
