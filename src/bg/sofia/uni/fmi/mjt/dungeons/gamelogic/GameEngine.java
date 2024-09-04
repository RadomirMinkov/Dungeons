package bg.sofia.uni.fmi.mjt.dungeons.gamelogic;

import bg.sofia.uni.fmi.mjt.dungeons.characters.ClassType;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NoSuchCharacterException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.ThereIsNoSuchUserException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.UserIsNotLoggedInException;
import bg.sofia.uni.fmi.mjt.dungeons.maps.Board;
import bg.sofia.uni.fmi.mjt.dungeons.user.Credentials;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.JsonReader;
import bg.sofia.uni.fmi.mjt.dungeons.utility.JsonWriter;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameEngine {

    List<User> allRegisteredUsers;
    Map<String, String> usersCredentials;
    List<User> activeUsers;
    Board gameBoard;
    JsonReader jsonReader;
    JsonWriter jsonWriter;

    {
        this.jsonReader = new JsonReader();
        this.jsonWriter = new JsonWriter();
        this.usersCredentials = new HashMap<>();
    }

    public GameEngine() {
        activeUsers = new ArrayList<>();
        jsonReader.readGameBoardFromJson(this.gameBoard);
        jsonReader.readUsersFromJson(allRegisteredUsers, usersCredentials);
    }

    public Board getGameBoard() {
        return gameBoard;
    }

    public Message deleteUser(String username, String password) throws ThereIsNoSuchUserException {
        if (usersCredentials.get(username) == null) {
            throw new ThereIsNoSuchUserException("No such user exist!");
        }
        if (!usersCredentials.get(username).equals(password)) {
            return new Message("Wrong password!");
        }
        allRegisteredUsers.removeIf(user -> user.getUsername().equals(username));
        return new Message("Successful deleting of a user!");
    }

    public Message createUser(String username, String password) {
        if (usersCredentials.get(username) != null) {
            return new Message("User with that username already exists!");
        }
        User newUser = new User(new Credentials(username, password), new HashMap<>());
        allRegisteredUsers.add(newUser);
        login(username, password);
        return new Message("Successful creation of a user! Hope that you will be having a great time!");
    }

    public Message login(String username, String password) {
        if (usersCredentials.get(username) == null) {
            return new Message("There is no such registered user!");
        }
        if (!usersCredentials.get(username).equals(password)) {
            return new Message("Password is wrong! Try again!");
        }
        for (User user : allRegisteredUsers) {
            if (user.getUsername().equals(username)) {
                activeUsers.add(user);
            }
        }
        return new Message("Successful login into your account");
    }

    public Message logout(User user) throws UserIsNotLoggedInException {
        assertUserIsLogged(user);
        activeUsers.remove(user);
        return new Message("You logged out successfully!");
    }

    public void assertUserIsLogged(User user) throws UserIsNotLoggedInException {
        if (!activeUsers.contains(user)) {
            throw new UserIsNotLoggedInException("This user is not logged in!");
        }
    }

    public Message deleteCharacter(User user, ClassType classType) throws NoSuchCharacterException {
        return user.deleteCharacter(classType);
    }

    public Message changeCharacter(User user, ClassType classType) {
        return user.changeCharacter(classType);
    }

    public Message createCharacter(User user, ClassType classType) {
        return user.createCharacter(classType);
    }
}
