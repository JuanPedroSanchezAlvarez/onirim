package com.misispiclix.onirim.domain.card;

import com.misispiclix.onirim.enums.Color;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("2")
public class DoorCard extends Card {

    @Enumerated(EnumType.ORDINAL)
    private Color color;

}
