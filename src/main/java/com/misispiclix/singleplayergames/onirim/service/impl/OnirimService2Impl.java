package com.misispiclix.singleplayergames.onirim.service.impl;

import com.misispiclix.singleplayergames.onirim.domain.Game;
import com.misispiclix.singleplayergames.onirim.dto.GameDTO;
import com.misispiclix.singleplayergames.onirim.service.IOnirimService;
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
    public void drawCardFromDeck(UUID id) { }

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
    public GameDTO createExample(GameDTO gameDTO) {
        return null;
    }

    @Override
    public Optional<GameDTO> updateExample(UUID id, GameDTO gameDTO) {
        return Optional.empty();
    }

    @Override
    public void updateExamplePatch(UUID id, GameDTO gameDTO) {

    }

    @Override
    public Boolean deleteExample(UUID id) {
        return null;
    }

}
