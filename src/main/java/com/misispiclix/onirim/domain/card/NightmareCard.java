package com.misispiclix.onirim.domain.card;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Entity
@DiscriminatorValue("3")
public class NightmareCard extends Card {
}
