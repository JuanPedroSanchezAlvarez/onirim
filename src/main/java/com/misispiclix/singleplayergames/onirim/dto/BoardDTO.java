package com.misispiclix.singleplayergames.onirim.dto;

import com.misispiclix.singleplayergames.onirim.dto.card.CardDTO;
import com.misispiclix.singleplayergames.onirim.dto.card.DoorCardDTO;
import com.misispiclix.singleplayergames.onirim.dto.card.LabyrinthCardDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardDTO {

    private List<CardDTO> cardDTODeck;
    private List<CardDTO> limboStack;
    private List<LabyrinthCardDTO> playedCards;
    private List<CardDTO> playerHand;
    private List<DoorCardDTO> discoveredDoors;
    private List<CardDTO> discardedCardDTOS;
    private List<CardDTO> cardsToShow;

    public BoardDTO() {
        this.cardDTODeck = new ArrayList<>();
        this.limboStack = new ArrayList<>();
        this.playedCards = new ArrayList<>();
        this.playerHand = new ArrayList<>();
        this.discoveredDoors = new ArrayList<>();
        this.discardedCardDTOS = new ArrayList<>();
        this.cardsToShow = new ArrayList<>();
    }

}
