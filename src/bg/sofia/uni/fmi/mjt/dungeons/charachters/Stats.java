package bg.sofia.uni.fmi.mjt.dungeons.charachters;

public interface Stats {
    int getMaxHealth();

    int getCurrentHealth();

    int getDefence();

    int getMaxMana();

    int getCurrentMana();

    int getAttack();

    void adjustCurrentHealth(int health);

    void adjustMaxHealth(int health);

    void adjustCurrentMana(int mana);

    void adjustMaxMana(int mana);

    void adjustAttack(int attack);

    void adjustDefence(int defence);

    void setHealth(int health);
}
