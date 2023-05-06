package com.misispiclix.singleplayergames.onirim.dto;

import com.misispiclix.singleplayergames.onirim.enums.AllowedAction;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class GameDTO extends BaseDTO {

    private BoardDTO board;
    private List<AllowedAction> allowedActions;
    private String messageToDisplay;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Integer version;

    public GameDTO() {
        this.board = new BoardDTO();
        this.allowedActions = new ArrayList<>();
        this.messageToDisplay = "";
    }

}
