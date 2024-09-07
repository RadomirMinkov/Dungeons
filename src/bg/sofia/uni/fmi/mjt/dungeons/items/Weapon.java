package bg.sofia.uni.fmi.mjt.dungeons.items;

import bg.sofia.uni.fmi.mjt.dungeons.utility.Constants;

import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.ZERO_POINT_TWO;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.ZERO_POINT_ZEROFIVE;

public class Weapon extends Treasure implements  Item {

    private double upgradePercent = ZERO_POINT_TWO;
    private int level;
    private double attack;

    public Weapon(String name) {
        super(name);
        this.level = 1;
        this.attack = Constants.FORTY;
    }

    public Weapon(String name, int level) {
        super(name);
        this.level = level;
        this.attack = Constants.FIFTY;
        for (int i = 1; i < level; i++) {
            upgradeItem();
        }
    }

    public Weapon(String name, int level, int attack) {
        super(name);
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
    public void upgradeItem() {
        level += 1;
        attack += attack * upgradePercent;
        upgradePercent += ZERO_POINT_ZEROFIVE;
    }
}
