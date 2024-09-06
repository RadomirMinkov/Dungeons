package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.characters.ClassType;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MapElementAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MapElementDoesNotExistException;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.maps.Board;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

public class ChangeCharacterCommand implements UserCommand {

    GameEngine gameEngine;
    User user;
    ClassType classType;
    Board gameBoard;

    public ChangeCharacterCommand(GameEngine gameEngine, User user, ClassType classType, Board gameBoard) {
        if (user == null) {
            throw new IllegalArgumentException("You are not logged in!");
        }
        if (gameEngine == null || classType == null || gameBoard == null) {
            throw new IllegalArgumentException("The arguments can't be null!");
        }
        this.gameEngine = gameEngine;
        this.user = user;
        this.classType = classType;
        this.gameBoard = gameBoard;
    }

    @Override
    public Message execute() {
        try {
            return gameEngine.changeCharacter(user, classType, gameBoard);
        } catch (MapElementAlreadyExistsException | MapElementDoesNotExistException e) {
            return new Message(e.getMessage());
        }
    }
}
