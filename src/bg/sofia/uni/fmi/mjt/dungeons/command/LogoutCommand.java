package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.UserIsNotLoggedInException;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

public class LogoutCommand implements UserCommand {
    GameEngine gameEngine;
    User user;

    public LogoutCommand(GameEngine gameEngine, User user) {
        if (gameEngine == null || user == null) {
            throw new IllegalArgumentException("The given arguments can't be null!");
        }
        this.gameEngine = gameEngine;
        this.user = user;

    }

    @Override
    public Message execute() {
        try {
            return gameEngine.logout(user);
        } catch (UserIsNotLoggedInException e) {
            return new Message(e.getMessage());
        }
    }
}
