package bg.sofia.uni.fmi.mjt.dungeons.characters;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.EmptyInventoryException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.FullBackPackException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.ItemNotFoundException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MissAttackException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NotEnoughExperienceException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.PlayerDiedAndResurrectedException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.PlayerDiedException;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.Mode;
import bg.sofia.uni.fmi.mjt.dungeons.items.*;
import bg.sofia.uni.fmi.mjt.dungeons.maps.Position;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CharacterTest {

    private Character character;
    private Position startPosition;
    private Weapon testWeapon;
    private BackPack testBackPack;

    private Item mockItem;
    private Actor mockEnemy;

    @BeforeEach
    void setUp() {
        startPosition = new Position(0, 0);
        testWeapon = new Weapon("Test Weapon", 10, 1); // Attack 10, Level 1
        testBackPack = new BackPack();
        character = new Character("Test Character", startPosition, 1, new Attributes(100, 100, 50, 50), 0, testBackPack, testWeapon);
        mockItem = mock(Item.class);
        mockEnemy = mock(Actor.class);
    }

    @Test
    void testLevelUpNotSuccessful() throws NotEnoughExperienceException {

        character = new Character("Test Character", startPosition, 1,
                new Attributes(100, 100, 50, 50), 150, testBackPack, testWeapon);

        character.levelUp();
        assertEquals(2, character.getLevel(), "Character level should increase to 2.");
    }

    @Test
    void testLevelUpNotEnoughExperience() {
        assertThrows(NotEnoughExperienceException.class, () -> character.levelUp());
    }

    @Test
    void testTakeDamage() throws MissAttackException, PlayerDiedException, PlayerDiedAndResurrectedException, EmptyInventoryException {
        AtomicInteger damageTaken = new AtomicInteger(0);
        character.takeDamage(60, damageTaken);

        assertEquals(90, character.getStats().getCurrentHealth(), "Health should be reduced to 10");
    }

    @Test
    void testTakeDamageMissAttack() {
        AtomicInteger damageTaken = new AtomicInteger(0);
        Exception exception = assertThrows(MissAttackException.class, () -> character.takeDamage(40, damageTaken));

        String expectedMessage = "The attack missed!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testResurrectPlayer() throws EmptyInventoryException, FullBackPackException {
        testBackPack.addElement(new HealthPotion());  // Add a health potion for resurrection

        Exception exception = assertThrows(PlayerDiedAndResurrectedException.class, () -> character.takeDamage(200, new AtomicInteger()));
        assertTrue(exception.getMessage().contains("resurrected dropping item"));
        assertTrue(character.getIsAlive(), "Character should still be alive after resurrection.");
    }

    @Test
    void testResurrectPlayerNoItems() {
        Exception exception = assertThrows(PlayerDiedException.class, () -> character.takeDamage(200, new AtomicInteger()));

        assertTrue(exception.getMessage().contains("died"));
        assertFalse(character.getIsAlive(), "Character should not be alive after dying with no items.");
    }

    @Test
    void testEquipWeaponSuccessful() throws NotEnoughExperienceException {
        Weapon newWeapon = new Weapon("New Weapon", 1, 1);
        Weapon oldWeapon = character.equipWeapon(newWeapon);

        assertEquals(newWeapon, character.getWeapon(), "New weapon should be equipped.");
        assertEquals(testWeapon, oldWeapon, "Old weapon should be returned.");
    }

    @Test
    void testEquipWeaponLevelTooLow() {
        Weapon highLevelWeapon = new Weapon("High Level Weapon", 20, 5);  // Level 5 weapon

        assertThrows(NotEnoughExperienceException.class, () -> character.equipWeapon(highLevelWeapon));
    }

    @Test
    void testUseHealthPotion() throws ItemNotFoundException, EmptyInventoryException, FullBackPackException {
        HealthPotion potion = new HealthPotion();
        testBackPack.addElement(potion);

        character.usePotion(potion);

        assertEquals(100, character.getStats().getCurrentHealth(), "Character's health should be 100.");
        testBackPack.addElement(potion);
        character.getStats().adjustCurrentHealth(-60);
        character.usePotion(potion);
        assertEquals(90, character.getStats().getCurrentHealth(), "Character's health should be 90.");

    }

    @Test
    void testUseManaPotion() throws ItemNotFoundException, EmptyInventoryException, FullBackPackException {
        ManaPotion potion = new ManaPotion();
        testBackPack.addElement(potion);

        character.usePotion(potion);

        assertEquals(100, character.getStats().getCurrentMana(), "Mana Should stay the same.");

        character.getStats().adjustCurrentMana(-60);
        testBackPack.addElement(potion);
        character.usePotion(potion);
        assertEquals(90, character.getStats().getCurrentMana(), "Mana Should stay the same.");
    }

    @Test
    void testUsePotionNotInInventory() {
        ManaPotion potion = new ManaPotion();

        assertThrows(ItemNotFoundException.class, () -> character.usePotion(potion));
    }

    @Test
    void testDropItemSuccessful() throws EmptyInventoryException, ItemNotFoundException, FullBackPackException {
        HealthPotion potion = new HealthPotion();
        testBackPack.addElement(potion);

        character.dropItem(potion);

        assertFalse(testBackPack.getElements().contains(potion), "Potion should be removed from inventory.");
    }

    @Test
    void testDropItemEmptyInInventory() {
        HealthPotion potion = new HealthPotion();

        assertThrows(EmptyInventoryException.class, () -> character.dropItem(potion));
    }
    @Test
    void testDropItemNotInInventory() throws FullBackPackException {
        HealthPotion potion = new HealthPotion();
        ManaPotion manaPotion = new ManaPotion();
        character.getInventory().addElement(manaPotion);
        assertThrows(ItemNotFoundException.class, () -> character.dropItem(potion));
    }
    @Test
    void testGetPosition() {
        assertEquals(startPosition, character.getPosition(), "Positions must be equal!");
    }
    @Test
    void testSetPosition() {
        Position position = new Position(1,1);
        character.setPosition(position);
        assertEquals(position, character.getPosition(), "Position must be equal!");
    }
    @Test
    void testUsePotionThatDoesNotExist() {
        Potion manaPotion = new HealthPotion();
        assertThrows(ItemNotFoundException.class, () -> character.usePotion(manaPotion),
                "Cant be used a potion that the player does not have!");
    }
    @Test
    void testGetName() {
        assertEquals("Test Character", character.getName(), "The name should be equal!");
    }
    @Test
    void testAttackSuccess() throws Exception {
        when(mockItem.getAttack()).thenReturn(20.0);
        AtomicInteger damageTaken = new AtomicInteger(0);

        doAnswer(invocation -> {
            damageTaken.set(70);
            return null;
        }).when(mockEnemy).takeDamage(anyDouble(), any(AtomicInteger.class));

        Message result = character.attack(mockItem, mockEnemy);

        assertEquals("You attacked the enemy for 0", result.message());
        assertEquals(Mode.BATTLE, result.mode());
    }

    @Test
    void testAttackMiss() throws Exception {
        when(mockItem.getAttack()).thenReturn(20.0);

        doThrow(new MissAttackException("The attack missed!")).when(mockEnemy).takeDamage(anyDouble(), any(AtomicInteger.class));

        Message result = character.attack(mockItem, mockEnemy);

        assertEquals("The attack missed!", result.message());
        assertEquals(Mode.BATTLE, result.mode());
    }

    @Test
    void testAttackWithOtherException() throws Exception {
        when(mockItem.getAttack()).thenReturn(20.0);

        doThrow(new RuntimeException("Something went wrong")).when(mockEnemy).takeDamage(anyDouble(), any(AtomicInteger.class));

        Message result = character.attack(mockItem, mockEnemy);

        assertEquals("Something went wrong", result.message());
        assertEquals(Mode.BATTLE, result.mode());
    }
    @Test
    void testPickUpItemBackpackFull() throws FullBackPackException {
        Treasure treasure = new HealthPotion();
        for (int i = 0; i < 10; i++) {
            testBackPack.addElement(treasure);
        }

        assertThrows(FullBackPackException.class, () -> character.pickUpItem(treasure));
    }

    @Test
    void testPickUpItemSuccessful() throws FullBackPackException {
        Treasure treasure = new HealthPotion();
        character.pickUpItem(treasure);

        assertTrue(testBackPack.getElements().contains(treasure), "Item should be added to the inventory.");
    }
}