package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.characters.ClassType;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NoSuchCharacterException;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

public class DeleteCharacter implements UserCommand {

    ClassType classType;
    GameEngine gameEngine;
    User user;

    public DeleteCharacter(GameEngine gameEngine, User user, ClassType classType) {
        if (user == null) {
            throw new IllegalArgumentException("You are not logged in!");
        }
        if (gameEngine == null || classType == null) {
            throw new IllegalArgumentException("The arguments can't be null!");
        }
        this.gameEngine = gameEngine;
        this.user = user;
        this.classType = classType;
    }

    @Override
    public Message execute() {
        try {
            return gameEngine.deleteCharacter(user, classType);
        } catch (NoSuchCharacterException e) {
            return new Message(e.getMessage());
        }
    }
}
