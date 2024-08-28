package bg.sofia.uni.fmi.mjt.dungeons.characters;

import bg.sofia.uni.fmi.mjt.dungeons.items.BackPack;
import bg.sofia.uni.fmi.mjt.dungeons.items.Weapon;
import bg.sofia.uni.fmi.mjt.dungeons.maps.Position;

public class MageClass extends Character {
    public MageClass(String name, Position position) {
        super(name, position);
    }

    public MageClass(String name, Position position, int level, Attributes stats,
                     int experience, BackPack inventory, Weapon weapon) {
        super(name, position, level, stats, experience, inventory, weapon);
    }
}
