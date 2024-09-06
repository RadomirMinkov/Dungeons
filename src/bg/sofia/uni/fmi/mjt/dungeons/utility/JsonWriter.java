package bg.sofia.uni.fmi.mjt.dungeons.utility;

import bg.sofia.uni.fmi.mjt.dungeons.maps.Board;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JsonWriter {
    public void writeUsersInformationToJson(List<User> allRegisteredUsers) {
        try (FileWriter writer = new FileWriter(Constants.JSON_CHARACTER_INFORMATION)) {
            Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
            gson1.toJson(allRegisteredUsers, writer);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public void writeMinionsInformationToJson(List<User> minions) {
        try (FileWriter writer = new FileWriter(Constants.JSON_MINION_INFORMATION)) {
            Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
            gson1.toJson(minions, writer);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public void writeGameBoardToJson(Board gameBoard) {
        try (FileWriter writer = new FileWriter(Constants.JSON_GAME_BOARD)) {
            Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
            gson1.toJson(gameBoard, writer);
        } catch (IOException e) {
            e.getMessage();
        }
    }
}
