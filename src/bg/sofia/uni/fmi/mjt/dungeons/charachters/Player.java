package bg.sofia.uni.fmi.mjt.dungeons.charachters;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.EmptyInventoryException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.FullBackPackException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.ItemNotFoundException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MinionDiedException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MissAttackException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NotEnoughExperienceException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.PlayerDiedAndResurrectedException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.PlayerDiedException;

import bg.sofia.uni.fmi.mjt.dungeons.items.HealthPotion;
import bg.sofia.uni.fmi.mjt.dungeons.items.Item;
import bg.sofia.uni.fmi.mjt.dungeons.items.ManaPotion;
import bg.sofia.uni.fmi.mjt.dungeons.items.Potion;
import bg.sofia.uni.fmi.mjt.dungeons.items.Weapon;

import bg.sofia.uni.fmi.mjt.dungeons.maps.BackPack;

import bg.sofia.uni.fmi.mjt.dungeons.maps.Position;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Pickable;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Constants;
import bg.sofia.uni.fmi.mjt.dungeons.utility.UsefulFunctions;

public class Player implements Actor {

    private int level;
    private Stats stats;
    private final String name;
    private BackPack inventory;
    private Weapon activeWeapon;

    private Position position;

    private boolean isAlive;
    private int experience;

    private int neededExperience;

    public Player(String name, Position position) {
        this.level = 1;
        this.stats = new Attributes(Constants.ONE_HUNDRED, Constants.ONE_HUNDRED,
                Constants.FIFTY, Constants.FIFTY);
        this.name = name;
        this.position = position;
        this.inventory = new BackPack();
        String weaponName = Constants.WEAPONS_NAMES
                .get(UsefulFunctions.genRandomNumber(0, Constants.WEAPONS_NAMES.size()));
        this.activeWeapon = new Weapon(weaponName);
        this.experience = 0;
        this.isAlive = true;
        this.neededExperience = Constants.ONE_HUNDRED;
    }

    public Player(String name, Position position, int level, Stats stats, int experience, BackPack inventory, Weapon weapon) {
        this.name = name;
        this.position = position;
        this.level = level;
        this.stats = stats;
        this.experience = experience;
        this.inventory = inventory;
        this.activeWeapon = weapon;
        this.isAlive = true;
        this.neededExperience = Constants.ONE_HUNDRED;
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
        return activeWeapon;
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
    public void takeDamage(double damage) throws MissAttackException, PlayerDiedAndResurrectedException,
            PlayerDiedException, EmptyInventoryException {
        double initialDamage = damage - stats.getDefence() * Constants.DEFENCE_MODIFIER;
        if (initialDamage <= 0) {
            throw new MissAttackException("The attack missed!");
        }
        stats.adjustCurrentHealth((int) Math.floor(initialDamage));
        if (stats.getCurrentHealth() < 0) {
            resurrectPlayer();
        }
    }

    public void resurrectPlayer() throws PlayerDiedException, PlayerDiedAndResurrectedException,
            EmptyInventoryException {
        if (inventory.getSize() <= 0) {
            isAlive = false;
            throw new PlayerDiedException(name + "died!");
        }
        stats.setHealth(Constants.FIFTY);
        int index = UsefulFunctions.genRandomNumber(0, inventory.getSize());

        Pickable droppedElement = inventory.removeElement(index);

        throw new PlayerDiedAndResurrectedException(
                name + "died. Then resurrected dropping item " + droppedElement.toString());
    }

    public void pickUpItem(Pickable item) throws FullBackPackException {
        inventory.addElement(item);
    }

    public void dropItem(Pickable item) throws EmptyInventoryException, ItemNotFoundException {
        inventory.removeElement(item);
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

    public Weapon equipWeapon(Weapon weapon) throws NotEnoughExperienceException {
        if (level < weapon.getLevel()) {
            throw new NotEnoughExperienceException("The level of the player is not big enough!");
        }
        Weapon oldWeapon = activeWeapon;
        activeWeapon = weapon;
        return oldWeapon;
    }

    public void usePotion(Potion potion) throws NotEnoughExperienceException,
            ItemNotFoundException, EmptyInventoryException {

        if (!inventory.getElements().contains(potion)) {
            throw new ItemNotFoundException("There are no such item in the inventory!");
        }
        if (potion instanceof HealthPotion) {
            inventory.removeElement(potion);
            stats.adjustCurrentHealth(potion.getPoints());
        } else if (potion instanceof ManaPotion) {
            inventory.removeElement(potion);
            stats.adjustCurrentMana(potion.getPoints());
        }
    }
}
