package com.misispiclix.onirim.service;

import com.misispiclix.onirim.dto.GameDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IOnirimService {
    Page<GameDTO> getGames(Integer pageNumber, Integer pageSize);
    Optional<GameDTO> getGameById(UUID id);
    UUID createNewGame();
    GameDTO saveGame(GameDTO gameDTO);

    void playCardFromHand(UUID id, Integer playedCardIndex);
    void discardCardFromHand(UUID id, Integer discardedCardIndex);
    void activateProphecy(UUID id);
    void confirmProphecy(UUID id, Integer discardedCardIndex, List<Integer> reorderedCardIndexes);
    void drawCardFromDeck(UUID id);
    void discardKeyCardFromHand(UUID id, Integer discardedCardIndex);
    void loseDoorCard(UUID id, Integer doorCardIndex);
    void discardTopCardsFromDeck(UUID id);
    void discardPlayerHand(UUID id);


    /*GameDTO createExample(GameDTO gameDTO);
    Optional<GameDTO> updateExample(UUID id, GameDTO gameDTO);
    void updateExamplePatch(UUID id, GameDTO gameDTO);
    Boolean deleteExample(UUID id);*/
}
