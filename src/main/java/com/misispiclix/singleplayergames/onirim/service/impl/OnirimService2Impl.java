package com.misispiclix.singleplayergames.onirim.service.impl;

import com.misispiclix.singleplayergames.onirim.domain.Game;
import com.misispiclix.singleplayergames.onirim.dto.GameDTO;
import com.misispiclix.singleplayergames.onirim.service.IOnirimService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Iterable<Game> getExamples() {
        return null;
    }

    @Override
    public Optional<Game> getExampleById(Long id) {
        return null;
    }

    @Override
    public Game createExample(Game game) {
        return null;
    }

    @Override
    public void updateExample(Long id, Game game) {

    }

    @Override
    public void updateExamplePatch(Long id, Game game) {

    }

    @Override
    public void deleteExample(Long id) {

    }

}
