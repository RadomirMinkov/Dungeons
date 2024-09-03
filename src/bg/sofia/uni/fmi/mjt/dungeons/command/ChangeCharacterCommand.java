package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.characters.ClassType;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

public class ChangeCharacterCommand implements UserCommand {

    GameEngine gameEngine;
    User user;
    ClassType classType;

    public ChangeCharacterCommand(GameEngine gameEngine, User user, ClassType classType) {
        this.gameEngine = gameEngine;
        this.user = user;
        this.classType = classType;
    }

    @Override
    public Message execute() {
        return gameEngine.changeCharacter(user, classType);
    }
}
