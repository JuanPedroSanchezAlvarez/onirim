package com.misispiclix.singleplayergames.onirim.domain;

import com.misispiclix.singleplayergames.onirim.enums.AllowedAction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@Entity
public class Game extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "board_id")
    private Board board;

    @ElementCollection(targetClass = AllowedAction.class, fetch = FetchType.EAGER)
    @JoinTable(name = "allowed_actions", joinColumns = @JoinColumn(name = "game_id"))
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "allowed_actions")
    private List<AllowedAction> allowedActions;

    @Column(name = "message_to_display")
    private String messageToDisplay;

}
