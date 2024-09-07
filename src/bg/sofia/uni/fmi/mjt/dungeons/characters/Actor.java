package bg.sofia.uni.fmi.mjt.dungeons.characters;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.EmptyInventoryException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MinionDiedException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MissAttackException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NotEnoughExperienceException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.PlayerDiedAndResurrectedException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.PlayerDiedException;

import bg.sofia.uni.fmi.mjt.dungeons.items.Item;
import bg.sofia.uni.fmi.mjt.dungeons.items.Weapon;
import bg.sofia.uni.fmi.mjt.dungeons.maps.Position;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import java.util.concurrent.atomic.AtomicInteger;

public interface Actor {

    int getLevel();

    Stats getStats();

    void levelUp() throws NotEnoughExperienceException;

    Weapon getWeapon();

    int getExperience();

    boolean getIsAlive();

    Message attack(Item item, Actor enemy) throws MinionDiedException, NotEnoughExperienceException;

    Position getPosition();

    void setPosition(Position position);

    Message takeDamage(double damage, AtomicInteger damageTaken, Actor actor)
            throws MissAttackException, PlayerDiedAndResurrectedException,
            PlayerDiedException, MinionDiedException, EmptyInventoryException;

}
