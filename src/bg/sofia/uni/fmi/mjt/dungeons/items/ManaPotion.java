package bg.sofia.uni.fmi.mjt.dungeons.items;

import bg.sofia.uni.fmi.mjt.dungeons.utility.Constants;

public class ManaPotion implements Treasure, Potion {
    private final int manaPoints;

    public ManaPotion() {
        manaPoints = Constants.FIFTY;
    }

    @Override
    public int getPoints() {
        return manaPoints;
    }
}
