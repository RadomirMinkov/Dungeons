package bg.sofia.uni.fmi.mjt.dungeons.utility;

import bg.sofia.uni.fmi.mjt.dungeons.items.ManaPotion;
import com.google.gson.Gson;

public class GsonTest {
    public static void main(String[] args) {
        Gson gson = new Gson();
        ManaPotion potion = new ManaPotion();
        String json = gson.toJson(potion);
        System.out.println(json);
        ManaPotion copiedPotion = gson.fromJson(json, ManaPotion.class);
        System.out.println(copiedPotion.getPoints());
        System.out.println(copiedPotion.toString());
    }
}
