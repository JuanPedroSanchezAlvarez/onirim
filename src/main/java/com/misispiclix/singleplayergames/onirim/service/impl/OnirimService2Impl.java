package com.misispiclix.singleplayergames.onirim.service.impl;

import com.misispiclix.singleplayergames.onirim.domain.Game;
import com.misispiclix.singleplayergames.onirim.dto.GameDTO;
import com.misispiclix.singleplayergames.onirim.service.IOnirimService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "onirimService2Impl")
public class OnirimService2Impl implements IOnirimService {

    @Override
    public GameDTO createNewGame() {
        return null;
    }

    @Override
    public GameDTO playCardFromHand(GameDTO gameDTO, Integer playedCardIndex) {
        return null;
    }

    @Override
    public GameDTO discardCardFromHand(GameDTO gameDTO, Integer discardedCardIndex) {
        return null;
    }

    @Override
    public GameDTO activateProphecy(GameDTO gameDTO) {
        return null;
    }

    @Override
    public GameDTO confirmProphecy(GameDTO gameDTO, Integer discardedCardIndex, List<Integer> reorderedCardIndexes) {
        return null;
    }

    @Override
    public GameDTO drawCardFromDeck(GameDTO gameDTO) {
        return null;
    }

    @Override
    public GameDTO discardKeyCardFromHand(GameDTO gameDTO, Integer discardedCardIndex) {
        return null;
    }

    @Override
    public GameDTO loseDoorCard(GameDTO gameDTO, Integer doorCardIndex) {
        return null;
    }

    @Override
    public GameDTO discardTopCardsFromDeck(GameDTO gameDTO) {
        return null;
    }

    @Override
    public GameDTO discardPlayerHand(GameDTO gameDTO) {
        return null;
    }

    @Override
    public Iterable<Game> getExample() {
        return null;
    }

}
