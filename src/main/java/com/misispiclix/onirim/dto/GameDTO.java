package com.misispiclix.onirim.dto;

import com.misispiclix.onirim.enums.AllowedAction;
import com.misispiclix.onirim.enums.GameStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class GameDTO extends BaseDTO {

    private BoardDTO board;
    private List<AllowedAction> allowedActions;
    private GameStatus gameStatus;
    private String messageToDisplay;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Integer version;
    private List<GameLogDTO> logs;

    public GameDTO() {
        this.board = new BoardDTO();
        this.allowedActions = new ArrayList<>();
        this.gameStatus = GameStatus.CREATED;
        this.messageToDisplay = "";
        this.logs = new ArrayList<>();
    }

}
