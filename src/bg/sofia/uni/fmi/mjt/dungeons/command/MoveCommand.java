package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.maps.Board;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

public class MoveCommand implements UserCommand {
    GameEngine gameEngine;
    Board board;

    public MoveCommand(GameEngine gameEngine, Board board) {
        this.gameEngine = gameEngine;
        this.board = board;
    }

    @Override
    public Message execute() {
        return null;
    }
}
