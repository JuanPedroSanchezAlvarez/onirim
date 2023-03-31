package com.misispiclix.singleplayergames.onirim.domain.card;

import com.misispiclix.singleplayergames.onirim.enums.Color;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
@Entity
@DiscriminatorValue("2")
public class DoorCard extends Card {

    @Enumerated(EnumType.ORDINAL)
    private Color color;

}
