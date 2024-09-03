package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

public class LoginCommand implements UserCommand {


    private final GameEngine gameEngine;
    private final String username;
    private final String password;

    public LoginCommand(GameEngine gameEngine, String username, String password) {
        if (gameEngine == null || username == null || password == null) {
            throw new IllegalArgumentException("The given arguments cannot be null!");
        }
        this.gameEngine = gameEngine;
        this.username = username;
        this.password = password;
    }

    @Override
    public Message execute() {
        return gameEngine.login(username, password);
    }
}
