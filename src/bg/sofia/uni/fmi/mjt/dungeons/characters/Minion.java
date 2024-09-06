package bg.sofia.uni.fmi.mjt.dungeons.characters;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MinionDiedException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MissAttackException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NotEnoughExperienceException;

import bg.sofia.uni.fmi.mjt.dungeons.items.Item;
import bg.sofia.uni.fmi.mjt.dungeons.items.Spell;
import bg.sofia.uni.fmi.mjt.dungeons.items.Weapon;

import bg.sofia.uni.fmi.mjt.dungeons.maps.Position;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Constants;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;
import bg.sofia.uni.fmi.mjt.dungeons.utility.UsefulFunctions;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.ATTACK_MODIFIER;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.DEFENCE_MODIFIER;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.ONE;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.TEN;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.THIRTY;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.TWO;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.ZERO_POINT_FIVE;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.ZERO_POINT_THREE;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.ZERO_POINT_TWO;

public class Minion implements Actor {

    private int level;
    private Attributes stats;

    private Weapon weapon;
    private Spell spell;
    private boolean isAlive;

    private int experience;
    private int neededExperience;
    private Position position;
    private int attackPowerUps;
    private int defencePowerUps;

    public Minion(int level, Position position) throws NotEnoughExperienceException {
        this.level = level;
        this.position = position;
        this.stats = new Attributes(Constants.SEVENTY, Constants.SEVENTY,
                Constants.THIRTY, Constants.THIRTY);
        String weaponName = Constants.WEAPONS_NAMES
                .get(UsefulFunctions.getRandomNumber(0, Constants.WEAPONS_NAMES.size()));
        this.weapon = new Weapon(weaponName);
        String spellName = Constants.SPELL_NAMES
                .get(UsefulFunctions.getRandomNumber(0, Constants.SPELL_NAMES.size()));
        this.spell = new Spell(spellName);
        isAlive = true;
        neededExperience = Constants.SEVENTY;
        this.experience = neededExperience * (level - 1);
        if (this.level != 1) {
            levelUp();
        }
    }

    public Minion(int level, Position position, Attributes stats,
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
    public Message attack(Item item, Actor enemy) {
        try {
            AtomicInteger damageTaken = new AtomicInteger(0);
            enemy.takeDamage(stats.getAttack() + item.getAttack(), damageTaken);
            return new Message("Minion attacked you for " + damageTaken);
        } catch (Exception e) {
            return new Message(e.getMessage());
        }
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public void takeDamage(double damage, AtomicInteger damageTaken) throws MissAttackException, MinionDiedException {
        double initialDamage = damage - stats.getDefence() * Constants.DEFENCE_MODIFIER;
        if (initialDamage <= 0) {
            throw new MissAttackException("The attack missed!");
        }
        stats.adjustCurrentHealth((int) Math.floor(initialDamage) * -1);
        damageTaken.addAndGet((int) Math.floor(initialDamage));
        if (stats.getCurrentHealth() < 0) {
            isAlive = false;
            throw new MinionDiedException("The Minion died!");
        }
    }

    public Action decideAction() {
        double healthPercentage = (double) stats.getCurrentHealth() / stats.getMaxHealth();
        double manaPercentage = (double) stats.getCurrentMana() / stats.getMaxMana();

        if (healthPercentage < ZERO_POINT_THREE && manaPercentage < ZERO_POINT_THREE) {
            return Action.DEFEND;
        }
        if (healthPercentage < ZERO_POINT_THREE && manaPercentage < ZERO_POINT_FIVE && stats.getCurrentMana() > 0) {
            return Action.POWER_UP;
        }
        if (manaPercentage < ZERO_POINT_TWO || stats.getCurrentMana() < TEN) {
            return Action.ATTACK_WITH_WEAPON;
        }
        return chooseAttack();
    }

    private Action chooseAttack() {
        Random random = new Random();

        if (stats.getCurrentMana() >= TEN) {
            return random.nextBoolean() ? Action.ATTACK_WITH_SPELL : Action.ATTACK_WITH_WEAPON;
        } else {
            return Action.ATTACK_WITH_WEAPON;
        }
    }

    public Message takeTurn(Actor actor) {
        Action action = decideAction();
        if (attackPowerUps > 0) {
            attackPowerUps--;
            if (attackPowerUps == 0) {
                stats.adjustAttack(-ATTACK_MODIFIER);
            }
        }
        if (defencePowerUps > 0) {
            defencePowerUps--;
            if (defencePowerUps == 0) {
                stats.adjustDefence(-DEFENCE_MODIFIER);
            }
        }
        return switch (action) {
            case ATTACK_WITH_WEAPON -> attack(weapon, actor);
            case ATTACK_WITH_SPELL -> attack(spell, actor);
            case DEFEND -> defend();
            case POWER_UP -> powerUp();
        };
    }

    private Message defend() {
        stats.adjustDefence(DEFENCE_MODIFIER);
        this.defencePowerUps += ONE;
        return new Message("Minion choose to defend himself for the next received attack!");
    }

    private Message powerUp() {
        stats.adjustAttack(ATTACK_MODIFIER);
        this.attackPowerUps += TWO;
        stats.adjustCurrentMana(-THIRTY);
        return new Message("Minion choose to power up for the next attack!");
    }

}
