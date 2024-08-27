package bg.sofia.uni.fmi.mjt.dungeons.user;

import bg.sofia.uni.fmi.mjt.dungeons.characters.Character;
import bg.sofia.uni.fmi.mjt.dungeons.characters.ClassType;

import java.util.Map;

public class User {

    private String username;
    private String password;

    private Map<ClassType, Character> characters;

    public User() {
    }

    public User(String username, String password, Map<ClassType, Character> characters) {
        this.username = username;
        this.password = password;
        this.characters = characters;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Map<ClassType, Character> getCharacters() {
        return characters;
    }

    public Character getCharacter(ClassType type) {
        return characters.get(type);
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateUsername(String newUsername) {
        this.username = newUsername;
    }

    public void login() {

    }

    public void logout() {

    }

    public void createCharacter() {

    }

    public void deleteCharacter() {

    }

    public void changeCharacter() {

    }

    public void deleteUser() {

    }

    public void createUser() {

    }
}
