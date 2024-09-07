package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.EmptyInventoryException;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.Mode;
import bg.sofia.uni.fmi.mjt.dungeons.items.Treasure;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import java.nio.channels.SelectionKey;
import java.util.List;

public class DropItemCommand implements UserCommand {
    GameEngine gameEngine;
    SelectionKey key;
    String word;

    public DropItemCommand(GameEngine gameEngine, SelectionKey key, String word) {
        if (null == gameEngine || null == key || word == null) {
            throw new IllegalArgumentException("The parameters shouldn't be null!");
        }
        this.gameEngine = gameEngine;
        this.key = key;
        this.word = word;
    }

    @Override
    public Message execute() {
        User user = (User) key.attachment();
        List<Treasure> treasureList = user.getCharacter(user.getActiveCharacter()).getInventory().getElements();
        int index = 0;
        for (Treasure treasure : treasureList) {
            if (treasure.getName().equals(word)) {
                break;
            }
            index++;
        }
        if (index >= treasureList.size()) {
            return new Message("There was no item to drop!", Mode.TRADE);
        }
        try {
            user.getCharacter(user.getActiveCharacter()).getInventory().removeElement(index);
            return new Message("You dropped " + word + "successfully and you can pick up the reasure!", Mode.TREASURE);
        } catch (EmptyInventoryException e) {
            return new Message(e.getMessage() + "You can pick the treasure!", Mode.TREASURE);
        }
    }
}
