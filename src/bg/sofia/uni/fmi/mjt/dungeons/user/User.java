package bg.sofia.uni.fmi.mjt.dungeons.user;

import bg.sofia.uni.fmi.mjt.dungeons.characters.Character;
import bg.sofia.uni.fmi.mjt.dungeons.characters.ClassType;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NoSuchCharacterException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.ThereIsNoSuchUserException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.UserIsNotLoggedInException;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import java.util.HashMap;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.dungeons.utility.GlobalVariables.activeUsers;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.GlobalVariables.allRegisteredUsers;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.GlobalVariables.usersCredentials;

public class User implements Comparable<User> {

    private Credentials credentials;

    private Map<ClassType, Character> characters;

    private ClassType activeCharacter;

    public User() {
    }

    public User(Credentials credentials, Map<ClassType, Character> characters) {
        this.credentials = credentials;
        this.characters = characters;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public String getUsername() {
        return credentials.getUsername();
    }

    public String getPassword() {
        return credentials.getPassword();
    }

    public int getCharacterCount() {
        return characters.size();
    }

    public Map<ClassType, Character> getCharacters() {
        return characters;
    }

    public Character getCharacter(ClassType type) {
        return characters.get(type);
    }

    public void updatePassword(String newPassword) {
        this.credentials.updatePassword(newPassword);
    }

    public void updateUsername(String newUsername) {
        this.credentials.updateUsername(newUsername);
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

    public void logout(User user) throws UserIsNotLoggedInException {
        assertUserIsLogged(user);
        activeUsers.remove(user);
    }

    public Message createCharacter(ClassType type) {
        if (characters.get(type) != null) {
            return new Message("You already character of this class!");
        }

        return new Message("Temporary solution!");
    }

    public Message deleteCharacter(ClassType type) throws NoSuchCharacterException {
        if (characters.get(type) == null) {
            throw new NoSuchCharacterException("There is no such character");
        }
        characters.remove(type);
        return new Message("Character successfully deleted!");
    }

    public Message changeCharacter(ClassType type) {
        if (type == activeCharacter) {
            return new Message("You are already using this character");
        }
        activeCharacter = type;
        return new Message("Switched to the " + type + "class!");
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
        activeUsers.add(newUser);
        return new Message("Successful creation of a user! Hope that you will be having a great time!");
    }

    @Override
    public int compareTo(User u) {
        return credentials.compareTo(u.credentials);
    }

    public void assertUserIsLogged(User user) throws UserIsNotLoggedInException {
        if (!activeUsers.contains(user)) {
            throw new UserIsNotLoggedInException("This user is not logged in!");
        }
    }
}
