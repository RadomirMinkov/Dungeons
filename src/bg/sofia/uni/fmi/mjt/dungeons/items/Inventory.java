package bg.sofia.uni.fmi.mjt.dungeons.items;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.EmptyInventoryException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.FullBackPackException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.ItemNotFoundException;

import java.util.List;

public interface Inventory {

    List<Treasure> getElements();

    int getSize();

    int getCapacity();

    void addElement(Treasure item) throws FullBackPackException;

    Treasure getElement(int index);

    void upgradeInventory();

    void removeElement(Treasure item) throws ItemNotFoundException, EmptyInventoryException;

    Treasure removeElement(int index) throws EmptyInventoryException;
}
