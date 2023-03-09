package com.misispiclix.singleplayergames.onirim.dto;

import com.misispiclix.singleplayergames.onirim.enums.ActionAllowed;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Game {

    private Board board;
    private List<ActionAllowed> actionsAllowed;

    public Game() {
        this.board = new Board();
        this.actionsAllowed = new ArrayList<>();
    }

}
