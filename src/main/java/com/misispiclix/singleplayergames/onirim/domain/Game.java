package com.misispiclix.singleplayergames.onirim.domain;

import com.misispiclix.singleplayergames.onirim.enums.AllowedAction;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
