package bg.sofia.uni.fmi.mjt.dungeons.utility;

import java.io.Serializable;

public record Message(String message) implements Serializable {

    void printMessage() {
        System.out.println(message);
    }
}
