package bg.sofia.uni.fmi.mjt.dungeons.items;

public interface Item {
    int getLevel();

    double getAttack();

    String getName();

    void upgradeItem();
}
