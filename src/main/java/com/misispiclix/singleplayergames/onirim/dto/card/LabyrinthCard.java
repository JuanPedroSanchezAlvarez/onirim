package com.misispiclix.singleplayergames.onirim.dto.card;

import com.misispiclix.singleplayergames.onirim.enums.Color;
import com.misispiclix.singleplayergames.onirim.enums.Symbol;
import lombok.Data;

@Data
public class LabyrinthCard extends Card {
    private Color color;
    private Symbol symbol;
    public LabyrinthCard(Color color, Symbol symbol) {
        this.color = color;
        this.symbol = symbol;
    }
}
