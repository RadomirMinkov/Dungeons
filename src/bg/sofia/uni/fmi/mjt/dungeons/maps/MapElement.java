package bg.sofia.uni.fmi.mjt.dungeons.maps;

public enum MapElement {
    OBSTACLE(1, "# "),

    PLAYER(2, "P"),
    MINION(3, "M"),
    TREASURE(4, "T"),
    FREE_SPACE(5, ".");

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
