package com.misispiclix.singleplayergames.onirim.dto;

import lombok.Data;

@Data
public class Game {

    private Board board;

    public Game() {
        this.board = new Board();
    }

}
