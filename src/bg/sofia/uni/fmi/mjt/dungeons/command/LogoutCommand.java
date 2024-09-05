package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.UserIsNotLoggedInException;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import java.nio.channels.SelectionKey;

public class LogoutCommand implements UserCommand {
    GameEngine gameEngine;
    SelectionKey key;
    public LogoutCommand(GameEngine gameEngine, SelectionKey key) {
        if (gameEngine == null || key == null) {
            throw new IllegalArgumentException("The given arguments can't be null!");
        }
        this.gameEngine = gameEngine;
        this.key = key;
    }

    @Override
    public Message execute() {
        try {
            return gameEngine.logout(key);
        } catch (UserIsNotLoggedInException e) {
            return new Message(e.getMessage());
        }
    }
}
