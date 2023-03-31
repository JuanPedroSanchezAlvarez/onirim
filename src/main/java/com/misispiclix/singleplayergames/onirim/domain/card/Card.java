package com.misispiclix.singleplayergames.onirim.domain.card;

import com.misispiclix.singleplayergames.onirim.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "card_type", discriminatorType = DiscriminatorType.INTEGER)
public abstract class Card extends BaseEntity {

}
