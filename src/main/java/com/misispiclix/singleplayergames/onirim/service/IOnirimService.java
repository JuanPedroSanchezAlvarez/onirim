package com.misispiclix.singleplayergames.onirim.service;

import com.misispiclix.singleplayergames.onirim.domain.Game;
import com.misispiclix.singleplayergames.onirim.dto.GameDTO;

import java.util.List;

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

    Iterable<Game> getExamples();
    Game getExampleById(Long id);
    Game createExample(Game game);
    void updateExample(Long id, Game game);
    void updateExamplePatch(Long id, Game game);
    void deleteExample(Long id);
}
