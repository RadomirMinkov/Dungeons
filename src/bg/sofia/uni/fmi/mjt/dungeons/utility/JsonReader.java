package bg.sofia.uni.fmi.mjt.dungeons.utility;

import bg.sofia.uni.fmi.mjt.dungeons.maps.Board;
import bg.sofia.uni.fmi.mjt.dungeons.maps.GameBoard;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.JSON_CHARACTER_INFORMATION;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.JSON_GAME_BOARD;

public class JsonReader {
    Gson gson;

    public JsonReader() {
        this.gson = new Gson();
    }

    public List<User> readUsersFromJson( Map<String, String> usersCredentials) {
        try (FileReader reader = new FileReader(JSON_CHARACTER_INFORMATION)) {
            Type listType = new TypeToken<List<User>>() {
            }.getType();
            List<User> allRegisteredUsers = this.gson.fromJson(reader, listType);

            for (User user : allRegisteredUsers) {
                usersCredentials.put(user.getUsername(), user.getPassword());
            }
            return allRegisteredUsers;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Board readGameBoardFromJson() {
        try (FileReader reader = new FileReader(JSON_GAME_BOARD)) {
            Board gameBoard = this.gson.fromJson(reader, GameBoard.class);
            return gameBoard;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
