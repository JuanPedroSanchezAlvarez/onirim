package com.misispiclix.singleplayergames.onirim.dto;

import com.misispiclix.singleplayergames.onirim.enums.AllowedAction;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameDTO {

    private BoardDTO boardDTO;
    private List<AllowedAction> allowedActions;
    private String messageToDisplay;

    public GameDTO() {
        this.boardDTO = new BoardDTO();
        this.allowedActions = new ArrayList<>();
        this.messageToDisplay = "";
    }

}
