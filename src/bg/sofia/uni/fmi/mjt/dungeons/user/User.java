package bg.sofia.uni.fmi.mjt.dungeons.user;

import bg.sofia.uni.fmi.mjt.dungeons.characters.Character;
import bg.sofia.uni.fmi.mjt.dungeons.characters.ClassType;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MapElementAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MapElementDoesNotExistException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NoSuchCharacterException;
import bg.sofia.uni.fmi.mjt.dungeons.maps.Board;
import bg.sofia.uni.fmi.mjt.dungeons.maps.MapElement;
import bg.sofia.uni.fmi.mjt.dungeons.maps.Position;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import java.util.Map;

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

    public ClassType getActiveCharacter() {
        return activeCharacter;
    }

    public void updatePassword(String newPassword) {
        this.credentials.updatePassword(newPassword);
    }

    public void updateUsername(String newUsername) {
        this.credentials.updateUsername(newUsername);
    }

    public Message createCharacter(ClassType type) {
        if (characters.get(type) != null) {
            return new Message("You already character of this class!");
        }
        characters.put(type, new Character("rado", new Position(2,4)));
        return new Message("Temporary solution!");
    }

    public Message deleteCharacter(ClassType type) throws NoSuchCharacterException {
        if (characters.get(type) == null) {
            throw new NoSuchCharacterException("There is no such character");
        }
        characters.remove(type);
        return new Message("Character successfully deleted!");
    }

    public Message changeCharacter(ClassType type, Board gameBoard) throws MapElementDoesNotExistException, MapElementAlreadyExistsException {
        if (null != activeCharacter && activeCharacter.equals(type)) {
            return new Message("You are already using this character");
        }
        if (characters.get(type) == null) {
            return new Message("You don't have a character of this class!");
        }
        if (activeCharacter != null) {
            gameBoard.getBoard().removeElement(MapElement.PLAYER,
                    characters.get(activeCharacter).getPosition().getRow(),
                    characters.get(activeCharacter).getPosition().getColumn());
        }
        gameBoard.getBoard().addElement(MapElement.PLAYER, characters.get(type).getPosition().getRow(),
                characters.get(type).getPosition().getColumn());
        activeCharacter = type;
        return new Message("Switched to the " + type + "class!");
    }

    @Override
    public int compareTo(User u) {
        return credentials.compareTo(u.credentials);
    }

}
