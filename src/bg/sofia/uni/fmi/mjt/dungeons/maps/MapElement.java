package bg.sofia.uni.fmi.mjt.dungeons.maps;

public enum MapElement {
    OBSTACLE(1),
    TREASURE(4),
    MINION(3),
    PLAYER(2),
    FREE_SPACE(5);

    private final int priority;

    MapElement(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
