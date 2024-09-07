package bg.sofia.uni.fmi.mjt.dungeons.characters;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.*;
import bg.sofia.uni.fmi.mjt.dungeons.items.Weapon;
import bg.sofia.uni.fmi.mjt.dungeons.items.Spell;
import bg.sofia.uni.fmi.mjt.dungeons.maps.Position;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Constants;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MinionTest {

    private Minion minion;
    private Position position;

    @BeforeEach
    void setUp() throws NotEnoughExperienceException {
        position = new Position(0, 0);
        minion = new Minion(1, position);
    }

    @Test
    void testConstructorSetsFieldsCorrectly() throws NotEnoughExperienceException {
        Minion testMinion = new Minion(1, position);
        assertEquals(1, testMinion.getLevel(), "Level should be initialized correctly");
        assertEquals(position, testMinion.getPosition(), "Position should be initialized correctly");
        assertEquals(Constants.SEVENTY, testMinion.getStats().getCurrentHealth(), "Health should be initialized correctly");
        assertEquals(Constants.SEVENTY, testMinion.getStats().getCurrentMana(), "Mana should be initialized correctly");
    }

    @Test
    void testLevelUpSuccess() throws NotEnoughExperienceException {
        minion = new Minion(1, position, new Attributes(100, 100, 20, 20), new Weapon("Sword"), new Spell("Fireball"), 100, 70);
        minion.levelUp();

        assertEquals(2, minion.getLevel(), "Level should increase");
        assertEquals(110, minion.getStats().getCurrentHealth(), "Health should increase by 10");
        assertEquals(110, minion.getStats().getCurrentMana(), "Mana should increase by 10");
    }

    @Test
    void testLevelUpThrowsException() {
        minion = new Minion(1, position, new Attributes(100, 100, 20, 20), new Weapon("Sword"), new Spell("Fireball"), 50, 70);
        assertThrows(NotEnoughExperienceException.class, minion::levelUp, "Should throw exception if not enough experience");
    }

    @Test
    void testGetWeapon() {
        assertNotNull(minion.getWeapon(), "Weapon should not be null after initialization");
    }

    @Test
    void testGetExperience() {
        assertEquals(0, minion.getExperience(), "Experience should be initialized to 0 for level 1");
    }

    @Test
    void testIsAlive() {
        assertTrue(minion.getIsAlive(), "Minion should be alive after initialization");
    }

    @Test
    void testAttackSuccessful() throws PlayerDiedAndResurrectedException, MissAttackException, MinionDiedException, EmptyInventoryException, PlayerDiedException {
        Weapon weapon = mock(Weapon.class);
        when(weapon.getAttack()).thenReturn(10.0);

        Actor enemy = mock(Actor.class);
        doNothing().when(enemy).takeDamage(anyDouble(), any(AtomicInteger.class));

        Message result = minion.attack(weapon, enemy);
        assertEquals("Minion attacked you for 0", result.message(), "Attack message should match expected output");
    }

    @Test
    void testAttackThrowsException() throws PlayerDiedAndResurrectedException, MissAttackException, MinionDiedException, EmptyInventoryException, PlayerDiedException {
        Weapon weapon = mock(Weapon.class);
        when(weapon.getAttack()).thenReturn(10.0);

        Actor enemy = mock(Actor.class);
        doThrow(new MissAttackException("The attack missed!")).when(enemy).takeDamage(anyDouble(), any(AtomicInteger.class));

        Message result = minion.attack(weapon, enemy);
        assertEquals("The attack missed!", result.message(), "Attack exception should be caught and return the proper message");
    }

    @Test
    void testTakeDamageSuccessful() throws MissAttackException, MinionDiedException {
        AtomicInteger damageTaken = new AtomicInteger(0);
        minion.takeDamage(70, damageTaken);

        assertEquals(40, damageTaken.get(), "Damage taken should be calculated correctly");
        assertEquals(30, minion.getStats().getCurrentHealth(), "Health should decrease by the correct amount");
    }

    @Test
    void testTakeDamageMissedAttack() {
        AtomicInteger damageTaken = new AtomicInteger(0);
        Exception exception = assertThrows(MissAttackException.class, () -> minion.takeDamage(0, damageTaken), "Should throw MissAttackException if damage is too low");

        assertEquals("The attack missed!", exception.getMessage(), "Exception message should match expected output");
    }

    @Test
    void testTakeDamageMinionDied() {
        AtomicInteger damageTaken = new AtomicInteger(0);
        Exception exception = assertThrows(MinionDiedException.class, () -> minion.takeDamage(1000, damageTaken), "Should throw MinionDiedException if health drops below 0");

        assertEquals("The Minion died!", exception.getMessage(), "Exception message should match expected output");
        assertFalse(minion.getIsAlive(), "Minion should be marked as dead");
    }

    @Test
    void testDecideActionDefend() {
        Attributes lowStats = new Attributes(10, 10, 10, 10);
        Minion lowHealthMinion = new Minion(1, position, lowStats, new Weapon("Sword"), new Spell("Fireball"), 0, 70);
        lowHealthMinion.getStats().adjustCurrentHealth(-8);
        lowHealthMinion.getStats().adjustCurrentMana(-8);
        Action action = lowHealthMinion.decideAction();

        assertEquals(Action.DEFEND, action, "Minion should decide to defend with low health and mana");
    }

    @Test
    void testDecideActionPowerUp() {
        Attributes lowStats = new Attributes(20, 40, 10, 10);
        Minion lowHealthMinion = new Minion(1, position, lowStats, new Weapon("Sword"), new Spell("Fireball"), 0, 70);
        lowHealthMinion.getStats().adjustCurrentHealth(-18);
        lowHealthMinion.getStats().adjustCurrentMana(-21);
        Action action = lowHealthMinion.decideAction();

        assertEquals(Action.POWER_UP, action, "Minion should decide to power up when health is low and some mana is available");
    }

    @Test
    void testTakeTurnAttack() {
        Actor actor = mock(Actor.class);
        when(actor.getPosition()).thenReturn(new Position(1, 1));

        Message message = minion.takeTurn(actor);
        assertTrue(message.message().contains("Minion attacked you"), "Minion should attack the actor");
    }

    @Test
    void testPowerUp() {
        Message result = minion.powerUp();

        assertEquals("Minion choose to power up for the next attack!", result.message(), "Power up message should be correct");
        assertEquals(2, minion.getAttackPowerUps(), "Minion should have attack power-ups after powering up");
    }

    @Test
    void testDefend() {
        Message result = minion.defend();

        assertEquals("Minion choose to defend himself for the next received attack!", result.message(), "Defend message should be correct");
        assertEquals(1, minion.getDefencePowerUps(), "Minion should have defence power-ups after defending");
    }

    @Test
    void testSetPosition() {
        Position newPosition = new Position(2, 2);
        minion.setPosition(newPosition);

        assertEquals(newPosition, minion.getPosition(), "Position should be updated correctly");
    }

}