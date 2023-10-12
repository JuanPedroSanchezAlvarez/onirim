package com.misispiclix.onirim.dto.card;

import com.misispiclix.onirim.enums.Color;
import lombok.Data;

@Data
public class DoorCardDTO extends CardDTO {

    private Color color;

    public DoorCardDTO(Color color) {
        this.color = color;
    }

}
