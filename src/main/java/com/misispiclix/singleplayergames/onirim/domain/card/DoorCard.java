package com.misispiclix.singleplayergames.onirim.domain.card;

import com.misispiclix.singleplayergames.onirim.enums.Color;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@DiscriminatorValue("2")
public class DoorCard extends Card {

    @Enumerated(EnumType.ORDINAL)
    private Color color;

}
