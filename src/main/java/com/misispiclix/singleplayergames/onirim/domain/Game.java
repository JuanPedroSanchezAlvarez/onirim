package com.misispiclix.singleplayergames.onirim.domain;

import com.misispiclix.singleplayergames.onirim.enums.AllowedAction;
import com.misispiclix.singleplayergames.onirim.enums.GameStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "game_status")
    private GameStatus gameStatus;

    @Column(name = "message_to_display")
    private String messageToDisplay;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;

    @Version
    private Integer version;

}
