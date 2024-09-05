package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import java.nio.channels.SelectionKey;

public class LoginCommand implements UserCommand {


    private final GameEngine gameEngine;
    private final String username;
    private final String password;

    private SelectionKey key;
    public LoginCommand(GameEngine gameEngine, String username, String password, SelectionKey key) {
        if (gameEngine == null || username == null || password == null || key == null) {
            throw new IllegalArgumentException("The given arguments cannot be null!");
        }
        this.gameEngine = gameEngine;
        this.username = username;
        this.password = password;
        this.key = key;
    }

    @Override
    public Message execute() {
        return gameEngine.login(username, password, key);
    }
}
