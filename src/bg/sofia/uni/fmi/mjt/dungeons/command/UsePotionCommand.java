package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.EmptyInventoryException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.ItemNotFoundException;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.Mode;
import bg.sofia.uni.fmi.mjt.dungeons.items.ManaPotion;
import bg.sofia.uni.fmi.mjt.dungeons.items.Potion;
import bg.sofia.uni.fmi.mjt.dungeons.user.User;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import java.nio.channels.SelectionKey;

public class UsePotionCommand implements UserCommand {
    SelectionKey key;
    String potionType;

    public UsePotionCommand(SelectionKey key, String potionType) {
        if (null == key || null == potionType) {
            throw new IllegalArgumentException("The parameters shouldn't be null!");
        }
        this.key = key;
        this.potionType = potionType;
    }

    @Override
    public Message execute() {
        User user = (User) key.attachment();
        Potion potion =  switch (potionType) {
            case "manaPotion" -> new ManaPotion();
            case "healthPotion" -> new ManaPotion();
            default -> null;
        };
        if (null == potion) {
            return new Message("No such potion exists", Mode.BATTLE, null);
        }
        try {
            user.getCharacter(user.getActiveCharacter()).usePotion(potion);
            return new Message("You used the potion successfully!", Mode.BATTLE, null);
        } catch (ItemNotFoundException | EmptyInventoryException e) {
            return new Message(e.getMessage(), Mode.BATTLE, null);
        }
    }
}
