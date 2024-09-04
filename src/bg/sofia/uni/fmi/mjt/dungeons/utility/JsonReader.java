package bg.sofia.uni.fmi.mjt.dungeons.utility;

import bg.sofia.uni.fmi.mjt.dungeons.maps.Board;
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

    public void readUsersFromJson(List<User> allRegisteredUsers, Map<String, String> usersCredentials) {
        try (FileReader reader = new FileReader(JSON_CHARACTER_INFORMATION)) {
            Type listType = new TypeToken<List<User>>() {
            }.getType();
            allRegisteredUsers = this.gson.fromJson(reader, listType);

            for (User user : allRegisteredUsers) {
                usersCredentials.put(user.getUsername(), user.getPassword());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void readGameBoardFromJson(Board gameBoard) {
        try (FileReader reader = new FileReader(JSON_GAME_BOARD)) {
            Type listType = new TypeToken<List<User>>() {
            }.getType();
            gameBoard = this.gson.fromJson(reader, listType);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
