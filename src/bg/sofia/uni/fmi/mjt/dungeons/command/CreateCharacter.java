package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

public class CreateCharacter implements UserCommand {
    GameEngine gameEngine;
    String username;
    String password;

    public CreateCharacter(GameEngine gameEngine, String username, String password) {
        if (gameEngine == null || username == null || password == null) {
            throw new IllegalArgumentException("The given arguments can't be null!");
        }
        this.gameEngine = gameEngine;
        this.username = username;
        this.password = password;
    }

    @Override
    public Message execute() {
        return gameEngine.createUser(username, password);
    }
}
