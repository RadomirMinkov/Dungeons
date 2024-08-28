package bg.sofia.uni.fmi.mjt.dungeons.items;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.EmptyInventoryException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.FullBackPackException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.ItemNotFoundException;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Pickable;

import java.util.List;

public interface Inventory {

    List<Pickable> getElements();

    int getSize();

    int getCapacity();

    void addElement(Pickable item) throws FullBackPackException;

    Pickable getElement(int index);

    void upgradeInventory();

    void removeElement(Pickable item) throws ItemNotFoundException, EmptyInventoryException;

    Pickable removeElement(int index) throws EmptyInventoryException;
}
