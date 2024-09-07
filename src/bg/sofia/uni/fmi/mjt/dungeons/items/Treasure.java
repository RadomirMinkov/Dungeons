package bg.sofia.uni.fmi.mjt.dungeons.items;

import bg.sofia.uni.fmi.mjt.dungeons.utility.Pickable;

public class Treasure implements Pickable {
    private String name;
    private boolean isOffered;

    {
        isOffered = false;
    }

    public Treasure(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Treasure other = (Treasure) o;
        return this.name.equals(other.name);
    }

    @Override
    public final int hashCode() {
        return name.hashCode();
    }

    public void setOffered() {
        isOffered = !isOffered;
    }

    public boolean getIsOffered() {
        return isOffered;
    }
}
