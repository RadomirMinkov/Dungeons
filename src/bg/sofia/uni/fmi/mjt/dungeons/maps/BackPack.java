package bg.sofia.uni.fmi.mjt.dungeons.maps;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.EmptyInventoryException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.FullBackPackException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.ItemNotFoundException;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Pickable;

import java.util.ArrayList;
import java.util.List;

public class BackPack implements Inventory {

    static final int TEN = 10;
    static final double UPGRADE_PERCENTAGE = 0.2;
    private List<Pickable> inventory;
    private int capacity;

    public BackPack() {
        inventory = new ArrayList<>();
        capacity = TEN;
    }

    @Override
    public List<Pickable> getElements() {
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
    public void addElement(Pickable item) throws FullBackPackException {
        if (inventory.size() == capacity) {
            throw new FullBackPackException("The Backpack is full!");
        }
        inventory.add(item);
    }

    @Override
    public Pickable getElement(int index) {
        return inventory.get(index);
    }

    @Override
    public void upgradeInventory() {
        capacity = (int) Math.round(capacity + capacity * UPGRADE_PERCENTAGE);
    }

    @Override
    public void removeElement(Pickable item) throws ItemNotFoundException, EmptyInventoryException {
        if (inventory.isEmpty()) {
            throw new EmptyInventoryException("The inventory is empty!");
        }
        if (!inventory.contains(item)) {
            throw new ItemNotFoundException("There is no such item in the Backpack!");
        }
        inventory.remove(item);
    }

    @Override
    public Pickable removeElement(int index) throws EmptyInventoryException {
        if (inventory.isEmpty()) {
            throw new EmptyInventoryException("The inventory is empty!");
        }
        if (index < 0 || index >= inventory.size()) {
            throw new IndexOutOfBoundsException("The index is out of bounds");
        }
        Pickable element = inventory.get(index);
        inventory.remove(index);
        return element;
    }
}
