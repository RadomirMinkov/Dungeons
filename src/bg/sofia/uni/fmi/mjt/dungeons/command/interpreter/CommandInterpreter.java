package bg.sofia.uni.fmi.mjt.dungeons.command.interpreter;

import bg.sofia.uni.fmi.mjt.dungeons.characters.ClassType;
import bg.sofia.uni.fmi.mjt.dungeons.command.ChangeCharacterCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.CreateCharacter;
import bg.sofia.uni.fmi.mjt.dungeons.command.CreateUserCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.DeleteCharacter;
import bg.sofia.uni.fmi.mjt.dungeons.command.DeleteUserCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.LoginCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.LogoutCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.MoveCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.ShowMapCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.UserCommand;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.UnknownCommandException;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.ONE;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.THREE;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.TWO;

public class CommandInterpreter {
    GameEngine gameEngine;

    public CommandInterpreter() {
        this.gameEngine = new GameEngine();
    }

    public CommandInterpreter(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public UserCommand intepretate(Message message, User user) throws UnknownCommandException {
        String[] words = message.message().split("\\s+");
        if (words.length == 0) {
            throw new UnknownCommandException("There is no empty commands without parameters!");
        } else if (ONE == words.length) {
            return switch (words[0]) {
                case "map" -> new ShowMapCommand(gameEngine, gameEngine.getGameBoard());
                case "logut" -> new LogoutCommand(gameEngine, new User());
                default -> throw new UnknownCommandException("Unknown command!");
            };
        } else if (TWO == words.length && words[0].equals("change")) {
            return new ChangeCharacterCommand(gameEngine, user, ClassType.valueOf(words[2]));
        } else if (THREE == words.length) {
            return switch (words[0]) {
                case "login" -> new LoginCommand(gameEngine, words[1], words[2]);
                case "move" -> new MoveCommand(gameEngine, gameEngine.getGameBoard());
                case "create" -> switch (words[1]) {
                    case "character" -> new CreateCharacter(gameEngine, user, ClassType.valueOf(words[2]));
                    case "user" -> new CreateUserCommand(gameEngine, words[1], words[2]);
                    default -> throw new UnknownCommandException("Unknown command!");
                };
                case "delete" -> switch (words[1]) {
                    case "character" -> new DeleteCharacter(gameEngine, user, ClassType.valueOf(words[2]));
                    case "user" -> new DeleteUserCommand(gameEngine, words[1], words[2]);
                    default -> throw new UnknownCommandException("Unknown command!");
                };
                default -> throw new UnknownCommandException("Unknown command!");
            };
        }
        throw new UnknownCommandException("Unknown command!");
    }

    public Message executeCommand(Message message, User user) {
        UserCommand command;
        try {
            command = intepretate(message, user);

        } catch (UnknownCommandException e) {
            return new Message(e.getMessage());
        }
        return command.execute();
    }
}
