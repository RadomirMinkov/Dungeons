package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.Mode;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

public class TradeCommand implements UserCommand {

    @Override
    public Message execute() {
        return new Message("You chose to trade!", Mode.TRADE);
    }
}
