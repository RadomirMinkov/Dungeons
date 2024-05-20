package bg.sofia.uni.fmi.mjt.dungeons.items;

import bg.sofia.uni.fmi.mjt.dungeons.utility.Constants;

public class HealthPotion implements Treasure, Potion {

    private final int healthPoints;

    public HealthPotion() {
        healthPoints = Constants.FIFTY;
    }

    @Override
    public int getPoints() {
        return healthPoints;
    }

}
