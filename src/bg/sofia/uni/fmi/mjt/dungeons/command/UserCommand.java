package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.UserIsNotLoggedInException;

public interface UserCommand {

    void execute() throws UserIsNotLoggedInException;
}
