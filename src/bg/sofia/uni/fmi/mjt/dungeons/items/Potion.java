package bg.sofia.uni.fmi.mjt.dungeons.items;

import bg.sofia.uni.fmi.mjt.dungeons.utility.Pickable;

public class Potion extends Treasure implements Pickable {
    private int points;

    public Potion(String name, int points) {
        super(name);
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
