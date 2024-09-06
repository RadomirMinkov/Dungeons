
import bg.sofia.uni.fmi.mjt.dungeons.characters.ClassType;
import bg.sofia.uni.fmi.mjt.dungeons.command.UserCommand;
import bg.sofia.uni.fmi.mjt.dungeons.command.interpreter.CommandInterpreter;
import bg.sofia.uni.fmi.mjt.dungeons.user.Credentials;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import java.nio.channels.SelectionKey;
import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Main {
    public static void test(int t) {
        t = 2;
    }

    public static void main(String[] args) {
        String test = "warrior";
        System.out.println(ClassType.fromString(test));

        SelectionKey mockKey = mock(SelectionKey.class);
        User mockUser = new User(new Credentials("rminkov", "Alienkiller832"), new HashMap<>());
        mockUser.createCharacter(ClassType.WARRIOR);
        when(mockKey.attachment()).thenReturn(mockUser);

        Message mockMessage = new Message("change character warrior");

        CommandInterpreter interpreter = new CommandInterpreter();
        interpreter.executeCommand(new Message("login rminkov Alienkiller832"), mockKey);
        System.out.println(interpreter.executeCommand(new Message("change character warrior"), mockKey).message());
        System.out.println(interpreter.executeCommand(new Message("map"), mockKey).message());

        int t = 1;
        test(t);
        System.out.println(t);
    }
}