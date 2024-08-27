package bg.sofia.uni.fmi.mjt.dungeons.utility;

import bg.sofia.uni.fmi.mjt.dungeons.characters.Character;
import bg.sofia.uni.fmi.mjt.dungeons.characters.ClassType;
import bg.sofia.uni.fmi.mjt.dungeons.items.ManaPotion;
import bg.sofia.uni.fmi.mjt.dungeons.maps.Position;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GsonTest {
    public static void main(String[] args) {
        Gson gson = new Gson();
        ManaPotion potion = new ManaPotion();
        String json = gson.toJson(potion);
        System.out.println(json);
        ManaPotion copiedPotion = gson.fromJson(json, ManaPotion.class);
        System.out.println(copiedPotion.getPoints());
        System.out.println(copiedPotion.toString());

        Position position = new Position(2, 3);
        Character me = new Character("radomir", position);
        List<User> users = new ArrayList<>();
        Map<ClassType, Character> r = new HashMap<>();
        r.put(ClassType.WARRIOR, me);
        users.add(new User("rminkov", "Alienkiller832", r));
        users.add(new User("nikola", "Alienkiller832", r));
        users.add(new User("MyPrecious", "Alienkiller832", r));
        String filePath = "C:\\Users\\HP\\IdeaProjects\\Dungeons\\src\\bg\\sofia\\uni\\fmi\\mjt\\dungeons\\utility\\CharachterInformation.json";
        try (FileWriter writer = new FileWriter(Constants.JSON_CHARACTER_INFORMATION)) {
            Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
            gson1.toJson(users, writer);
            System.out.println("User has been written to " + Constants.JSON_CHARACTER_INFORMATION);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
