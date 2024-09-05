
import bg.sofia.uni.fmi.mjt.dungeons.characters.ClassType;
import bg.sofia.uni.fmi.mjt.dungeons.command.interpreter.CommandInterpreter;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

public class Main {
    public static void main(String[] args) {
        String test = "warrior";
        System.out.println(ClassType.fromString(test));

        CommandInterpreter interpreter = new CommandInterpreter();
        System.out.println(interpreter.executeCommand(new Message("change character warrior"), null).message());
    }
}