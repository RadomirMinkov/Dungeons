package bg.sofia.uni.fmi.mjt.dungeons.command.interpreter;

import bg.sofia.uni.fmi.mjt.dungeons.characters.ClassType;
import bg.sofia.uni.fmi.mjt.dungeons.command.AcceptItemCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.AttackCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.ChangeCharacterCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.CreateCharacter;
import bg.sofia.uni.fmi.mjt.dungeons.command.CreateUserCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.DefendCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.DeleteCharacter;
import bg.sofia.uni.fmi.mjt.dungeons.command.DeleteUserCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.DoNothingCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.DropItemCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.FightCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.LoginCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.LogoutCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.MoveCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.OfferCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.PickUpCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.PowerUpCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.PutDownCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.SeeInventoryCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.ShowMapCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.TradeCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.UsePotionCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.UserCommand;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MapElementAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.UnknownCommandException;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.Mode;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import java.nio.channels.SelectionKey;

import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.FOUR;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.ONE;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.THREE;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.TWO;

public class CommandInterpreter {
    GameEngine gameEngine;

    public CommandInterpreter() throws MapElementAlreadyExistsException {
        this.gameEngine = new GameEngine();
    }

    public CommandInterpreter(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public UserCommand intepretate(Message message, SelectionKey key) throws UnknownCommandException {
        return switch (message.mode()) {

            case NORMAL -> intepretateNormal(message, key);
            case BATTLE -> intepretateBattle(message, key);
            case TRADE -> intepretateTrade(message, key);
            case TREASURE -> intepretateTreasure(message, key);
            case CHOOSE -> intepretateChoose(message, key);
        };
    }

    private UserCommand intepretateBattle(Message message, SelectionKey key) throws UnknownCommandException {
        String[] words =  message.message().isBlank() ? new String[0] : message.message().split("\\s+");
        if (0 == words.length) {
            throw new UnknownCommandException("There is no empty commands without parameters!");
        } else if (ONE == words.length) {
            return switch (words[0]) {
                case "attack" -> new AttackCommand(gameEngine, key, message.user());
                case "defend" -> new DefendCommand();
                default -> throw new UnknownCommandException("Unknown command!");
            };
        } else if (TWO == words.length && words[0].equals("power")) {
            return new PowerUpCommand();

        } else if (THREE == words.length && words[0].equals("use") && words[1].equals("potion")) {
            return new UsePotionCommand(key, words[2]);
        }
        throw new UnknownCommandException("Unknown command!");
    }

    private UserCommand intepretateChoose(Message message, SelectionKey key) throws UnknownCommandException {
        String[] words =  message.message().isBlank() ? new String[0] : message.message().split("\\s+");
        if (0 == words.length) {
            throw new UnknownCommandException("There is no empty commands without parameters!");
        } else if (ONE == words.length) {
            return switch (words[0]) {
                case "trade" -> new TradeCommand();
                case "fight" -> new FightCommand();
                case "nothing" -> new DoNothingCommand();
                default -> throw new UnknownCommandException("Unknown command!");
            };
        }
        throw new UnknownCommandException("Unknown command!");
    }

    private UserCommand intepretateTrade(Message message, SelectionKey key) throws UnknownCommandException {
        String[] words =  message.message().isBlank() ? new String[0] : message.message().split("\\s+");
        if (0 == words.length) {
            throw new UnknownCommandException("There is no empty commands without parameters!");
        } else if (TWO == words.length) {
            return switch (words[0]) {
                case "offer" -> new OfferCommand(gameEngine, key, words[1]);
                case "accept" -> new AcceptItemCommand(gameEngine, key, words[1]);
                default -> throw new UnknownCommandException("Unknown command!");
            };
        }
        throw new UnknownCommandException("Unknown command!");
    }

    private UserCommand intepretateTreasure(Message message, SelectionKey key) throws UnknownCommandException {
        String[] words =  message.message().isBlank() ? new String[0] : message.message().split("\\s+");
        if (0 == words.length) {
            throw new UnknownCommandException("There is no empty commands without parameters!");
        } else if (TWO == words.length && words[0].equals("pick") && words[1].equals("up")) {
            return new PickUpCommand(gameEngine, key);
        } else if (TWO == words.length && words[0].equals("put") && words[1].equals("down")) {
            return new PutDownCommand(gameEngine, key);
        } else if (TWO == words.length && words[0].equals("drop")) {
            return new DropItemCommand(gameEngine, key, words[1]);
        }
        throw new UnknownCommandException("Unknown command!");
    }

    public UserCommand intepretateNormal(Message message, SelectionKey key) throws UnknownCommandException {
        String[] words =  message.message().isBlank() ? new String[0] : message.message().split("\\s+");
        if (words.length == 0) {
            throw new UnknownCommandException("There is no empty commands without parameters!");
        } else if (ONE == words.length) {
            return normalUserCommandOneWord(key, words);
        } else if (TWO == words.length && words[0].equals("move")) {
            return new MoveCommand(gameEngine, key, words[1]);
        } else if (THREE == words.length) {
            return switch (words[0]) {
                case "change", "choose" -> new ChangeCharacterCommand(gameEngine, (User) key.attachment(),
                        ClassType.fromString(words[2]), gameEngine.getGameBoard());
                case "login" -> new LoginCommand(gameEngine, words[1], words[TWO], key);
                case "create" ->
                        new CreateCharacter(gameEngine, (User) key.attachment(), ClassType.fromString(words[2]));
                case "delete" ->
                        new DeleteCharacter(gameEngine, (User) key.attachment(), ClassType.fromString(words[2]));
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

    private UserCommand normalUserCommandOneWord(SelectionKey key, String[] words) throws UnknownCommandException {
        return switch (words[0]) {
            case "map" -> new ShowMapCommand(gameEngine, gameEngine.getGameBoard());
            case "logout" -> new LogoutCommand(gameEngine, key);
            case "inventory" -> new SeeInventoryCommand(gameEngine, key);
            default -> throw new UnknownCommandException("Unknown command!");
        };
    }

    public Message executeCommand(Message message, SelectionKey key) {
        UserCommand command;
        try {
            command = intepretate(message, key);

        } catch (UnknownCommandException | IllegalArgumentException e) {
            return new Message(e.getMessage(), Mode.NORMAL, null);
        }
        return command.execute();
    }
}
