package bg.sofia.uni.fmi.mjt.dungeons.characters;

public class Attributes implements Stats {
    private int maxHealth;
    private int currentHealth;
    private int maxMana;
    private int currentMana;
    private int attack;
    private int defence;

    public Attributes(int maxHealth, int maxMana, int attack, int defence) {
        this.maxHealth = maxHealth;
        this.currentHealth = this.maxHealth;
        this.maxMana = maxMana;
        this.currentMana = this.maxMana;
        this.attack = attack;
        this.defence = defence;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public int getCurrentHealth() {
        return currentHealth;
    }

    @Override
    public int getDefence() {
        return defence;
    }

    @Override
    public int getMaxMana() {
        return maxMana;
    }

    @Override
    public int getCurrentMana() {
        return currentMana;
    }

    @Override
    public int getAttack() {
        return attack;
    }

    @Override
    public void adjustCurrentHealth(int health) {
        if (currentHealth + health <= maxHealth) {
            currentHealth = currentHealth + health;
        } else {
            currentHealth = maxHealth;
        }
    }

    @Override
    public void adjustMaxHealth(int health) {
        maxHealth += health;
    }

    @Override
    public void adjustCurrentMana(int mana) {
        if (currentMana + mana <= maxMana) {
            currentMana = currentMana + mana;
        } else {
            currentMana = maxMana;
        }
    }

    @Override
    public void adjustMaxMana(int mana) {
        maxMana = maxMana + mana;
    }

    @Override
    public void adjustAttack(int att) {
        attack += att;
    }

    @Override
    public void adjustDefence(int def) {
        defence -= def;
    }

    @Override
    public void setHealth(int hp) {
        if (hp > maxHealth) {
            currentHealth = maxHealth;
        } else {
            currentHealth = hp;
        }
    }

}
