package com.misispiclix.singleplayergames.onirim.dto.card;

import com.misispiclix.singleplayergames.onirim.enums.Color;
import lombok.Data;

@Data
public class DoorCard extends Card {
    private Color color;
    public DoorCard(Color color) {
        this.color = color;
    }
}
