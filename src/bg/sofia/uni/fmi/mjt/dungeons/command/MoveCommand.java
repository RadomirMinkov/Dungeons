package bg.sofia.uni.fmi.mjt.dungeons.command;

import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.GameEngine;
import bg.sofia.uni.fmi.mjt.dungeons.maps.Board;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import java.nio.channels.SelectionKey;

public class MoveCommand implements UserCommand {
    private GameEngine gameEngine;
    private Board board;
    private SelectionKey key;

    public MoveCommand(GameEngine gameEngine, Board board, SelectionKey key) {
        if (gameEngine == null || board == null || key == null) {
            throw new IllegalArgumentException("The given arguments cannot be null!");
        }
        this.gameEngine = gameEngine;
        this.board = board;
        this.key = key;
    }

    @Override
    public Message execute() {
        return null;
    }
}
