package bg.sofia.uni.fmi.mjt.dungeons.utility;

import java.util.Random;

public class UsefulFunctions {

    public static int genRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
}
