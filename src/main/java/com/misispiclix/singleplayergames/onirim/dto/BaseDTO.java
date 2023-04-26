package com.misispiclix.singleplayergames.onirim.dto;

import lombok.Data;

import java.util.UUID;

@Data
public abstract class BaseDTO {

    private UUID id;
    private Integer version;

}
