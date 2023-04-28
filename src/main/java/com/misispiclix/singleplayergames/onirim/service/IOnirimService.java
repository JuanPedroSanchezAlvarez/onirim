package com.misispiclix.singleplayergames.onirim.service;

import com.misispiclix.singleplayergames.onirim.domain.Game;
import com.misispiclix.singleplayergames.onirim.dto.GameDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IOnirimService {
    GameDTO createNewGame();
    GameDTO playCardFromHand(GameDTO gameDTO, Integer playedCardIndex);
    GameDTO discardCardFromHand(GameDTO gameDTO, Integer discardedCardIndex);
    GameDTO activateProphecy(GameDTO gameDTO);
    GameDTO confirmProphecy(GameDTO gameDTO, Integer discardedCardIndex, List<Integer> reorderedCardIndexes);
    GameDTO drawCardFromDeck(GameDTO gameDTO);
    GameDTO discardKeyCardFromHand(GameDTO gameDTO, Integer discardedCardIndex);
    GameDTO loseDoorCard(GameDTO gameDTO, Integer doorCardIndex);
    GameDTO discardTopCardsFromDeck(GameDTO gameDTO);
    GameDTO discardPlayerHand(GameDTO gameDTO);

    List<GameDTO> getExamples();
    Optional<GameDTO> getExampleById(UUID id);
    GameDTO createExample(GameDTO gameDTO);
    Optional<GameDTO> updateExample(UUID id, GameDTO gameDTO);
    void updateExamplePatch(UUID id, GameDTO gameDTO);
    Boolean deleteExample(UUID id);
}
