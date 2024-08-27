package bg.sofia.uni.fmi.mjt.dungeons.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;

public class JsonWriter {
    void writeUsersInformationToJson() {
        try (FileWriter writer = new FileWriter(Constants.JSON_CHARACTER_INFORMATION)) {
            Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
            gson1.toJson(GlobalVariables.allRegisteredUsers, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
