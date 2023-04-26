package com.misispiclix.singleplayergames.onirim.domain.card;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.misispiclix.singleplayergames.onirim.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DoorCard.class, name = "doorCard"),
        @JsonSubTypes.Type(value = LabyrinthCard.class, name = "labyrinthCard"),
        @JsonSubTypes.Type(value = NightmareCard.class, name = "nightmareCard")
})
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "card_type", discriminatorType = DiscriminatorType.INTEGER)
public abstract class Card extends BaseEntity {

}
