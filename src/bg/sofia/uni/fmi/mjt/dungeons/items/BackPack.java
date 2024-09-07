package bg.sofia.uni.fmi.mjt.dungeons.items;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.EmptyInventoryException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.FullBackPackException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.ItemNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.TEN;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.UPGRADE_PERCENTAGE;

public class BackPack implements Inventory {

    private List<Treasure> inventory;
    private int capacity;

    public BackPack() {
        inventory = new ArrayList<>();
        capacity = TEN;
    }

    @Override
    public List<Treasure> getElements() {
        return inventory;
    }

    @Override
    public int getSize() {
        return inventory.size();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public void addElement(Treasure item) throws FullBackPackException {
        if (inventory.size() >= capacity) {
            throw new FullBackPackException("The Backpack is full! You should drop item to be able to pick another!");
        }
        inventory.add(item);
    }

    @Override
    public Treasure getElement(int index) {
        return inventory.get(index);
    }

    @Override
    public void upgradeInventory() {
        capacity = (int) Math.round(capacity + capacity * UPGRADE_PERCENTAGE);

    }

    @Override
    public void removeElement(Treasure item) throws ItemNotFoundException, EmptyInventoryException {
        if (inventory.isEmpty()) {
            throw new EmptyInventoryException("The inventory is empty!");
        }
        if (!inventory.contains(item)) {
            throw new ItemNotFoundException("There is no such item in the Backpack!");
        }
        inventory.remove(item);
    }

    @Override
    public Treasure removeElement(int index) throws EmptyInventoryException {
        if (inventory.isEmpty()) {
            throw new EmptyInventoryException("The inventory is empty!");
        }
        if (index < 0 || index >= inventory.size()) {
            throw new IndexOutOfBoundsException("The index is out of bounds");
        }
        Treasure element = inventory.get(index);
        inventory.remove(index);
        return element;
    }
}
