package bg.sofia.uni.fmi.mjt.dungeons.charachters;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.EmptyInventoryException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MinionDiedException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MissAttackException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NotEnoughExperienceException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.PlayerDiedAndResurrectedException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.PlayerDiedException;

import bg.sofia.uni.fmi.mjt.dungeons.items.Item;
import bg.sofia.uni.fmi.mjt.dungeons.items.Weapon;
import bg.sofia.uni.fmi.mjt.dungeons.maps.Position;

public interface Actor {

    int getLevel();

    Stats getStats();

    void levelUp() throws NotEnoughExperienceException;

    Weapon getWeapon();

    int getExperience();

    boolean getIsAlive();

    void attack(Item item, Actor actor) throws MissAttackException, PlayerDiedAndResurrectedException,
            EmptyInventoryException, PlayerDiedException, MinionDiedException;

    Position getPosition();

    void takeDamage(double damage) throws MissAttackException, PlayerDiedAndResurrectedException,
            PlayerDiedException, EmptyInventoryException, MinionDiedException;

}
