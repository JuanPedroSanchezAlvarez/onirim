package com.misispiclix.singleplayergames.onirim.service;

import com.misispiclix.singleplayergames.onirim.dto.GameDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IOnirimService {
    List<GameDTO> getGames();
    Optional<GameDTO> getGameById(UUID id);
    UUID createNewGame();
    GameDTO saveGame(GameDTO gameDTO);
    GameDTO playCardFromHand(GameDTO gameDTO, Integer playedCardIndex);
    GameDTO discardCardFromHand(GameDTO gameDTO, Integer discardedCardIndex);
    GameDTO activateProphecy(GameDTO gameDTO);
    GameDTO confirmProphecy(GameDTO gameDTO, Integer discardedCardIndex, List<Integer> reorderedCardIndexes);
    GameDTO drawCardFromDeck(GameDTO gameDTO);
    GameDTO discardKeyCardFromHand(GameDTO gameDTO, Integer discardedCardIndex);
    GameDTO loseDoorCard(GameDTO gameDTO, Integer doorCardIndex);
    GameDTO discardTopCardsFromDeck(GameDTO gameDTO);
    GameDTO discardPlayerHand(GameDTO gameDTO);


    GameDTO createExample(GameDTO gameDTO);
    Optional<GameDTO> updateExample(UUID id, GameDTO gameDTO);
    void updateExamplePatch(UUID id, GameDTO gameDTO);
    Boolean deleteExample(UUID id);
}
