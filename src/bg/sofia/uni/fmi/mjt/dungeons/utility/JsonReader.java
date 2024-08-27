package bg.sofia.uni.fmi.mjt.dungeons.utility;

import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

public class JsonReader {

    void readUsersFromJson() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(Constants.JSON_CHARACTER_INFORMATION)) {
            Type userListType = new TypeToken<List<User>>() {
            }.getType();
            GlobalVariables.allRegisteredUsers = gson.fromJson(reader, userListType);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
