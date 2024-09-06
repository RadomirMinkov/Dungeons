package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

public class DoNothingCommand implements UserCommand {
    @Override
    public Message execute() {
        return null;
    }
}
