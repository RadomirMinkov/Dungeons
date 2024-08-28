package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.UserIsNotLoggedInException;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;

public class LogoutCommand implements UserCommand {
    User user;

    LogoutCommand(User user) {
        this.user = user;
    }

    @Override
    public void execute() throws UserIsNotLoggedInException {
        user.logout(user);
    }
}
