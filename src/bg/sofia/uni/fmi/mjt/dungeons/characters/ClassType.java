package bg.sofia.uni.fmi.mjt.dungeons.characters;

public enum ClassType {
    WARRIOR("warrior"),
    WIZARD("wizard"),
    ROGUE("rogue");

    private final String type;

    ClassType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static ClassType fromString(String description) {
        for (ClassType type : ClassType.values()) {
            if (type.getType().equals(description)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No class type that is equal to " + description);
    }

    @Override
    public String toString() {
        return String.valueOf(type);
    }
}
