package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.ThereIsNoSuchUserException;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

public class DeleteUserCommand implements UserCommand {
    GameEngine gameEngine;
    String username;
    String password;

    public DeleteUserCommand(GameEngine gameEngine, String username, String password) {
        this.gameEngine = gameEngine;
        this.username = username;
        this.password = password;
    }

    @Override
    public Message execute() {
        try {
            return gameEngine.deleteUser(username, password);
        } catch (ThereIsNoSuchUserException e) {
            return new Message(e.getMessage());
        }
    }
}
