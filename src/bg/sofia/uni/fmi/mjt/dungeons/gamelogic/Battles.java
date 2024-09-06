package bg.sofia.uni.fmi.mjt.dungeons.gamelogic;

import bg.sofia.uni.fmi.mjt.dungeons.characters.Action;
import bg.sofia.uni.fmi.mjt.dungeons.characters.Actor;

public class Battles {
    private Actor characterOne;
    private Actor characterTwo;

    public Battles(Actor characterOne, Actor characterTwo) {
        while (characterOne.getStats().getCurrentHealth() > 0 || characterTwo.getStats().getCurrentHealth() > 0) {

        }
    }

    public Action enemyAction(Actor character) {
        return null;
    }
}
