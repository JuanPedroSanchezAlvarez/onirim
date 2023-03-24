package com.misispiclix.singleplayergames.onirim.dto.card;

import com.misispiclix.singleplayergames.onirim.enums.Color;
import com.misispiclix.singleplayergames.onirim.enums.Symbol;
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
