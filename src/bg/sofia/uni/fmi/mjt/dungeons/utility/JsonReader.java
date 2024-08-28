package bg.sofia.uni.fmi.mjt.dungeons.utility;

import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.TreeMap;

import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.JSON_CHARACTER_INFORMATION;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.GlobalVariables.allRegisteredUsers;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.GlobalVariables.usersCredentials;

public class JsonReader {

    void readUsersFromJson() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(JSON_CHARACTER_INFORMATION)) {
            Type listType = new TypeToken<List<User>>() {
            }.getType();
            allRegisteredUsers = gson.fromJson(reader, listType);

            for (User user : allRegisteredUsers) {
                usersCredentials.put(user.getUsername(), user.getPassword());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
