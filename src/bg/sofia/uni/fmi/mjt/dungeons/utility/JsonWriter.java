package bg.sofia.uni.fmi.mjt.dungeons.utility;

import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JsonWriter {
    void writeUsersInformationToJson(List<User> allRegisteredUsers) {
        try (FileWriter writer = new FileWriter(Constants.JSON_CHARACTER_INFORMATION)) {
            Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
            gson1.toJson(allRegisteredUsers, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
