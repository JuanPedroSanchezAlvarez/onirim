package com.misispiclix.singleplayergames.onirim.dto.card;

import com.misispiclix.singleplayergames.onirim.enums.Color;
import lombok.Data;

@Data
public class DoorCardDTO extends CardDTO {

    private Color color;

    public DoorCardDTO(Color color) {
        this.color = color;
    }

}
