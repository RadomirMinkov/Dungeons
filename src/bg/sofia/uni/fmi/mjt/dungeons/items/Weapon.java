package bg.sofia.uni.fmi.mjt.dungeons.items;

import bg.sofia.uni.fmi.mjt.dungeons.utility.Constants;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Pickable;

import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.ZERO_POINT_TWO;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.ZERO_POINT_ZEROFIVE;

public class Weapon implements Pickable, Treasure, Item {

    private double upgradePercent = ZERO_POINT_TWO;
    private int level;
    private double attack;
    private String name;

    public Weapon(String name) {
        this.name = name;
        this.level = 1;
        this.attack = Constants.FORTY;
    }

    public Weapon(String name, int level) {
        this.name = name;
        this.level = level;
        this.attack = Constants.FIFTY;
        for (int i = 1; i < level; i++) {
            upgradeItem();
        }
    }

    public Weapon(String name, int level, int attack) {
        this.name = name;
        this.level = level;
        this.attack = attack;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public double getAttack() {
        return attack;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void upgradeItem() {
        level += 1;
        attack = attack + attack * upgradePercent;
        upgradePercent += ZERO_POINT_ZEROFIVE;
    }

}
