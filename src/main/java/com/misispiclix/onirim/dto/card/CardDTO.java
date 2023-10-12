package com.misispiclix.onirim.dto.card;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.misispiclix.onirim.dto.BaseDTO;
import lombok.Data;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DoorCardDTO.class, name = "doorCard"),
        @JsonSubTypes.Type(value = LabyrinthCardDTO.class, name = "labyrinthCard"),
        @JsonSubTypes.Type(value = NightmareCardDTO.class, name = "nightmareCard")
})
@Data
public abstract class CardDTO extends BaseDTO {
}
