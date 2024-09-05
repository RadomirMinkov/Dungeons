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

import java.nio.channels.SelectionKey;

import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.FOUR;
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

    public UserCommand intepretate(Message message, SelectionKey key) throws UnknownCommandException {
        String[] words = message.message().split("\\s+");
        if (words.length == 0) {
            throw new UnknownCommandException("There is no empty commands without parameters!");
        } else if (ONE == words.length) {
            return switch (words[0]) {
                case "map" -> new ShowMapCommand(gameEngine, gameEngine.getGameBoard());
                case "logout" -> new LogoutCommand(gameEngine, key);
                default -> throw new UnknownCommandException("Unknown command!");
            };
        } else if (TWO == words.length && words[0].equals("move")) {
            return new MoveCommand(gameEngine, gameEngine.getGameBoard(), key);
        } else if (THREE == words.length) {
            return switch (words[0]) {
                case "change", "choose" ->
                        new ChangeCharacterCommand(gameEngine, (User) key.attachment(),
                                ClassType.fromString(words[2]), gameEngine.getGameBoard());
                case "login" -> new LoginCommand(gameEngine, words[1], words[TWO], key);
                case "create" -> new CreateCharacter(gameEngine, (User) key.attachment(), ClassType.valueOf(words[2]));
                case "delete" -> new DeleteCharacter(gameEngine, (User) key.attachment(), ClassType.valueOf(words[2]));
                default -> throw new UnknownCommandException("Unknown command!");
            };
        } else if (FOUR == words.length && words[1].equals("user")) {
            return switch (words[0]) {
                case "create" -> new CreateUserCommand(gameEngine, words[TWO], words[THREE], key);
                case "delete" -> new DeleteUserCommand(gameEngine, words[TWO], words[THREE], key);
                default -> throw new UnknownCommandException("Unknown command!");
            };
        } else {
            throw new UnknownCommandException("Unknown command!");
        }
    }

    public Message executeCommand(Message message, SelectionKey key) {
        UserCommand command;
        try {
            command = intepretate(message, key);

        } catch (UnknownCommandException | IllegalArgumentException e) {
            return new Message(e.getMessage());
        }
        return command.execute();
    }
}
