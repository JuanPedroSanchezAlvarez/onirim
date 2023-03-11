package com.misispiclix.singleplayergames.onirim.dto;

import com.misispiclix.singleplayergames.onirim.enums.AllowedAction;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Game {

    private Board board;
    private List<AllowedAction> allowedActions;
    private String messageToDisplay;

    public Game() {
        this.board = new Board();
        this.allowedActions = new ArrayList<>();
        this.messageToDisplay = "";
    }

}
