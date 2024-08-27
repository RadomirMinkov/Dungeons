package bg.sofia.uni.fmi.mjt.dungeons.characters;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.EmptyInventoryException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MinionDiedException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MissAttackException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NotEnoughExperienceException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.PlayerDiedAndResurrectedException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.PlayerDiedException;

import bg.sofia.uni.fmi.mjt.dungeons.items.Item;
import bg.sofia.uni.fmi.mjt.dungeons.items.Spell;
import bg.sofia.uni.fmi.mjt.dungeons.items.Weapon;

import bg.sofia.uni.fmi.mjt.dungeons.maps.Position;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Constants;
import bg.sofia.uni.fmi.mjt.dungeons.utility.UsefulFunctions;

public class Minion implements Actor {

    private int level;
    private Stats stats;

    private Weapon weapon;
    private Spell spell;
    private boolean isAlive;

    private int experience;
    private int neededExperience;
    private Position position;

    public Minion(int level, Position position) throws NotEnoughExperienceException {
        this.level = level;
        this.position = position;
        this.stats = new Attributes(Constants.SEVENTY, Constants.SEVENTY,
                Constants.THIRTY, Constants.THIRTY);
        String weaponName = Constants.WEAPONS_NAMES
                .get(UsefulFunctions.genRandomNumber(0, Constants.WEAPONS_NAMES.size()));
        this.weapon = new Weapon(weaponName);
        String spellName = Constants.SPELL_NAMES
                .get(UsefulFunctions.genRandomNumber(0, Constants.SPELL_NAMES.size()));
        this.spell = new Spell(spellName);
        isAlive = true;
        this.experience = neededExperience * (level - 1);
        neededExperience = Constants.SEVENTY;
        levelUp();
    }

    public Minion(int level, Position position, Stats stats,
                  Weapon weapon, Spell spell, int experience, int neededExperience) {
        this.level = level;
        this.position = position;
        this.stats = stats;
        this.weapon = weapon;
        this.spell = spell;
        this.experience = experience;
        this.isAlive = true;
        this.neededExperience = neededExperience;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public Stats getStats() {
        return stats;
    }

    @Override
    public void levelUp() throws NotEnoughExperienceException {
        if (experience < neededExperience) {
            throw new NotEnoughExperienceException("You don't have enough experience to level up!");
        }
        while (experience >= neededExperience) {
            experience -= neededExperience;
            level += 1;
            stats.adjustCurrentHealth(Constants.TEN);
            stats.adjustCurrentMana(Constants.TEN);
            stats.adjustDefence(Constants.FIFE);
            stats.adjustAttack(Constants.FIFE);
            neededExperience += (int) Math.round(neededExperience * Constants.LEVEL_UP_MULTIPLIER);
        }
    }

    @Override
    public Weapon getWeapon() {
        return weapon;
    }

    @Override
    public int getExperience() {
        return experience;
    }

    @Override
    public boolean getIsAlive() {
        return isAlive;
    }

    @Override
    public void attack(Item item, Actor enemy) throws MissAttackException, PlayerDiedAndResurrectedException,
            EmptyInventoryException, PlayerDiedException, MinionDiedException {
        enemy.takeDamage(stats.getAttack() + item.getAttack());
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void takeDamage(double damage) throws MissAttackException, MinionDiedException {
        double initialDamage = damage - stats.getDefence() * Constants.DEFENCE_MODIFIER;
        if (initialDamage <= 0) {
            throw new MissAttackException("The attack missed!");
        }
        stats.adjustCurrentHealth((int) Math.floor(initialDamage));
        if (stats.getCurrentHealth() < 0) {
            isAlive = false;
            throw new MinionDiedException("The Minion died!");
        }
    }

}
