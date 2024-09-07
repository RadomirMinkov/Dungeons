package bg.sofia.uni.fmi.mjt.dungeons.utility;

import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.Mode;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;

import java.io.Serializable;

public record Message(String message, Mode mode, User user) implements Serializable {

    void printMessage() {
        System.out.println(message);
    }
}
