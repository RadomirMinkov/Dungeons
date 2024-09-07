
import bg.sofia.uni.fmi.mjt.dungeons.characters.ClassType;
import bg.sofia.uni.fmi.mjt.dungeons.command.interpreter.CommandInterpreter;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MapElementAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.Mode;
import bg.sofia.uni.fmi.mjt.dungeons.maps.MapElement;
import bg.sofia.uni.fmi.mjt.dungeons.user.Credentials;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.CustomPair;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.PriorityQueue;

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

        Message mockMessage = new Message("change character warrior", Mode.NORMAL);
        try {
            CommandInterpreter interpreter = new CommandInterpreter();
            interpreter.executeCommand(new Message("login rminkov Alienkiller832", Mode.NORMAL), mockKey);
            System.out.println(interpreter.executeCommand(
                    new Message("change character warrior", Mode.NORMAL), mockKey).message());
            System.out.println(interpreter.executeCommand(new Message("map", Mode.NORMAL), mockKey).message());
            System.out.println(interpreter.executeCommand(new Message("move up", Mode.NORMAL), mockKey).message());
            System.out.println(interpreter.executeCommand(new Message("map", Mode.NORMAL), mockKey).message());

            PriorityQueue<CustomPair> pr = new PriorityQueue<>();
            pr.add(new CustomPair(MapElement.OBSTACLE, null));
            pr.add(new CustomPair(MapElement.OBSTACLE, null));
            for (CustomPair p : pr) {
                System.out.println(p.equals(new CustomPair(MapElement.OBSTACLE, null)));
            }
        } catch (MapElementAlreadyExistsException e) {
            System.out.println("Failed initialisation");
        }
    }
}