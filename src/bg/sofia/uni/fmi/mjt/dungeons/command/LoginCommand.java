package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.user.User;

public class LoginCommand implements UserCommand {

    private final User user;
    private final String username;
    private final String password;

    public LoginCommand(User user, String username, String password) {
        if (user == null || username == null || password == null) {
            throw new IllegalArgumentException("The given arguments cannot be null!");
        }
        this.user = user;
        this.username = username;
        this.password = password;
    }

    @Override
    public void execute() {
        user.login(username, password);
    }
}
