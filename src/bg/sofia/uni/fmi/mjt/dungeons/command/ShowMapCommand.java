package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.Mode;
import bg.sofia.uni.fmi.mjt.dungeons.maps.Board;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

public class ShowMapCommand implements UserCommand {
    GameEngine gameEngine;
    Board gameBoard;

    public ShowMapCommand(GameEngine gameEngine, Board gameBoard) {
        this.gameEngine = gameEngine;
        this.gameBoard = gameBoard;
    }

    @Override
    public Message execute() {
        return new Message(gameBoard.boardAsString().toString(), Mode.NORMAL);
    }
}
