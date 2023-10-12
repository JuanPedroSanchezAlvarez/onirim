package com.misispiclix.onirim.domain.card;

import com.misispiclix.onirim.enums.Color;
import com.misispiclix.onirim.enums.Symbol;
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
@DiscriminatorValue("1")
public class LabyrinthCard extends Card {

    @Enumerated(EnumType.ORDINAL)
    private Color color;

    @Enumerated(EnumType.ORDINAL)
    private Symbol symbol;

}
