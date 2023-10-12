package com.misispiclix.onirim.service.impl;

import com.misispiclix.onirim.dto.GameDTO;
import com.misispiclix.onirim.service.IOnirimService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service(value = "onirimService2Impl")
public class OnirimService2Impl implements IOnirimService {

    @Override
    public Page<GameDTO> getGames(Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Override
    public Optional<GameDTO> getGameById(UUID id) {
        return Optional.empty();
    }

    @Override
    public UUID createNewGame() {
        return null;
    }

    @Override
    public GameDTO saveGame(GameDTO gameDTO) {
        return null;
    }

    @Override
    public void playCardFromHand(UUID id, Integer playedCardIndex) { }

    @Override
    public void discardCardFromHand(UUID id, Integer discardedCardIndex) { }

    @Override
    public void activateProphecy(UUID id) { }

    @Override
    public void confirmProphecy(UUID id, Integer discardedCardIndex, List<Integer> reorderedCardIndexes) { }

    @Override
    public void drawCardFromDeck(UUID id) { }

    @Override
    public void discardKeyCardFromHand(UUID id, Integer discardedCardIndex) { }

    @Override
    public void loseDoorCard(UUID id, Integer doorCardIndex) { }

    @Override
    public void discardTopCardsFromDeck(UUID id) { }

    @Override
    public void discardPlayerHand(UUID id) { }

}
