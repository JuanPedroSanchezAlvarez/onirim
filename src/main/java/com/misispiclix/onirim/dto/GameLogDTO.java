package com.misispiclix.onirim.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GameLogDTO extends BaseDTO {

    private LocalDateTime created;
    private String message;

    public GameLogDTO(String message) {
        this.message = message;
    }

}
