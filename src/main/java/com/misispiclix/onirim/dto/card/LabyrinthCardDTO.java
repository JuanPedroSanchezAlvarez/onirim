package com.misispiclix.onirim.dto.card;

import com.misispiclix.onirim.enums.Color;
import com.misispiclix.onirim.enums.Symbol;
import lombok.Data;

@Data
public class LabyrinthCardDTO extends CardDTO {

    private Color color;
    private Symbol symbol;

    public LabyrinthCardDTO(Color color, Symbol symbol) {
        this.color = color;
        this.symbol = symbol;
    }

}
