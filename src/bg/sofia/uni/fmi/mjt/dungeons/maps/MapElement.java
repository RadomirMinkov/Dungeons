package bg.sofia.uni.fmi.mjt.dungeons.maps;

public enum MapElement {
    OBSTACLE(1, "X"),
    TREASURE(4, "\t&#9670"),
    MINION(3, "E"),
    PLAYER(2, "P"),
    FREE_SPACE(5, "O");

    private final int priority;
    private final String symbol;

    MapElement(int priority, String symbol) {
        this.priority = priority;
        this.symbol = symbol;
    }

    public int getPriority() {
        return priority;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return String.valueOf(symbol);
    }
}
