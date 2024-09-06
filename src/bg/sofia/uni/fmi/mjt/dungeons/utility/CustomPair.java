package bg.sofia.uni.fmi.mjt.dungeons.utility;

import bg.sofia.uni.fmi.mjt.dungeons.characters.Actor;
import bg.sofia.uni.fmi.mjt.dungeons.maps.MapElement;

public class CustomPair implements Comparable<CustomPair> {
    MapElement mapElement;
    Actor actor;

    public CustomPair(MapElement mapElement, Actor actor) {
        this.mapElement = mapElement;
        this.actor = actor;
    }

    public MapElement getMapElement() {
        return mapElement;
    }

    public Actor getMappedObject() {
        return actor;
    }

    public void setMapElement(MapElement mapElement) {
        if (mapElement != null) {
            this.mapElement = mapElement;
        }
    }

    public void setMappedObject(Actor mappedObject) {
        if (mappedObject != null) {
            this.mapElement = mapElement;
        }
    }

    @Override
    public int compareTo(CustomPair o) {
        return mapElement.compareTo(o.mapElement);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomPair other = (CustomPair) o;
        return this.mapElement.equals(other.mapElement);
    }

    @Override
    public final int hashCode() {
        return mapElement.hashCode();
    }
}
