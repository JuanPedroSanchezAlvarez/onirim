package com.misispiclix.singleplayergames.onirim.controller;

import com.misispiclix.singleplayergames.onirim.dto.GameDTO;
import com.misispiclix.singleplayergames.onirim.service.IOnirimService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class OnirimController {

    @Qualifier(value = "onirimServiceImpl")
    private final IOnirimService onirimService;

    public GameDTO createNewGame() {
        return onirimService.createNewGame();
    }

    public GameDTO playCardFromHand(GameDTO gameDTO, Integer playedCardIndex) {
        return onirimService.playCardFromHand(gameDTO, playedCardIndex);
    }

    public GameDTO discardCardFromHand(GameDTO gameDTO, Integer discardedCardIndex) {
        return onirimService.discardCardFromHand(gameDTO, discardedCardIndex);
    }

    public GameDTO activateProphecy(GameDTO gameDTO) {
        return onirimService.activateProphecy(gameDTO);
    }

    public GameDTO confirmProphecy(GameDTO gameDTO, Integer discardedCardIndex, List<Integer> reorderedCardIndexes) {
        return onirimService.confirmProphecy(gameDTO, discardedCardIndex, reorderedCardIndexes);
    }

    public GameDTO drawCardFromDeck(GameDTO gameDTO) {
        return onirimService.drawCardFromDeck(gameDTO);
    }

    public GameDTO discardKeyCardFromHand(GameDTO gameDTO, Integer discardedCardIndex) {
        return onirimService.discardKeyCardFromHand(gameDTO, discardedCardIndex);
    }

    public GameDTO loseDoorCard(GameDTO gameDTO, Integer doorCardIndex) {
        return onirimService.loseDoorCard(gameDTO, doorCardIndex);
    }

    public GameDTO discardTopCardsFromDeck(GameDTO gameDTO) {
        return onirimService.discardTopCardsFromDeck(gameDTO);
    }

    public GameDTO discardPlayerHand(GameDTO gameDTO) {
        return onirimService.discardPlayerHand(gameDTO);
    }

    @GetMapping(path = "/example")
    public String getExample(Model model) {
        model.addAttribute("games", onirimService.getExamples());
        return "example";
    }

}
