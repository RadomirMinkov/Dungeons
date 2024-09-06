package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.characters.ClassType;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

public class CreateCharacter implements UserCommand {
    private GameEngine gameEngine;
    private User user;
    private ClassType classType;

    public CreateCharacter(GameEngine gameEngine, User user, ClassType classType) {
        if (gameEngine == null || user == null || classType == null) {
            throw new IllegalArgumentException("The given arguments can't be null!");
        }
        this.gameEngine = gameEngine;
        this.user = user;
        this.classType = classType;
    }

    @Override
    public Message execute() {
        return gameEngine.createCharacter(user, classType);
    }
}
