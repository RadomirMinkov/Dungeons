package bg.sofia.uni.fmi.mjt.dungeons.items;

import bg.sofia.uni.fmi.mjt.dungeons.utility.Constants;

import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.ZERO_POINT_ONE;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.ZERO_POINT_THREE;

public class Spell implements Treasure, Item {

    private double upgradePercent = ZERO_POINT_THREE;
    private int level;
    private double attack;

    private int manaCost;
    private String name;

    public Spell(String name) {
        this.name = name;
        this.level = 1;
        this.attack = Constants.FIFTY;
        this.manaCost = Constants.THIRTY;
    }

    public Spell(String name, int level) {
        this.name = name;
        this.level = level;
        this.attack = Constants.FIFTY;
        this.manaCost = Constants.THIRTY;
        for (int i = 1; i < level; i++) {
            upgradeItem();
        }
    }

    public Spell(String name, int level, double attack, int manaCost) {
        this.name = name;
        this.level = level;
        this.attack = attack;
        this.manaCost = manaCost;
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

    public int getManaCost() {
        return manaCost;
    }

    @Override
    public void upgradeItem() {
        level += 1;
        attack = attack + attack * upgradePercent;
        manaCost = (int) Math.round(manaCost + manaCost * ZERO_POINT_ONE);
        upgradePercent += ZERO_POINT_ONE;
    }
}
