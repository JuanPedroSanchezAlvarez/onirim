package com.misispiclix.singleplayergames.onirim.service;

import com.misispiclix.singleplayergames.onirim.dto.GameDTO;
import com.misispiclix.singleplayergames.onirim.dto.card.LabyrinthCardDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class OnirimServiceTest {

    @InjectMocks
    IOnirimService onirimService;

    @Test
    void getGames() {
        // PREPARATION
        onirimService.createNewGame();
        onirimService.createNewGame();
        onirimService.createNewGame();
        // EXECUTION
        Page<GameDTO> pageOfGameDto = onirimService.getGames(0, 10);
        // VERIFICATION
        assertThat(pageOfGameDto.getNumberOfElements()).isEqualTo(3);
    }

    @Test
    void getGameById() {
        // PREPARATION
        UUID id = onirimService.createNewGame();
        // EXECUTION
        Optional<GameDTO> optionalOfGameDto = onirimService.getGameById(id);
        // VERIFICATION
        assertThat(optionalOfGameDto).isNotEmpty();
    }

    @Test
    void createNewGame() {
        // PREPARATION
        // EXECUTION
        UUID id = onirimService.createNewGame();
        Optional<GameDTO> optionalOfGameDto = onirimService.getGameById(id);
        assertThat(optionalOfGameDto).isNotEmpty();
        GameDTO gameDTO = optionalOfGameDto.get();
        // VERIFICATION
        // The card deck must have 71 cards.
        assertThat(gameDTO.getBoard().getCardDeck().size()).isEqualTo(71);
        // The player hand must have 5 cards.
        assertThat(gameDTO.getBoard().getPlayerHand().size()).isEqualTo(5);
        // The 5 cards of the player hand must be Labyrinth cards.
        gameDTO.getBoard().getPlayerHand().forEach(card -> {
            assertEquals(card.getClass(), LabyrinthCardDTO.class);
        });
        // The other zones must be empty.
        assertThat(gameDTO.getBoard().getCardsToShow().isEmpty());
        assertThat(gameDTO.getBoard().getDiscardedCards().isEmpty());
        assertThat(gameDTO.getBoard().getDiscoveredDoors().isEmpty());
        assertThat(gameDTO.getBoard().getLimboStack().isEmpty());
        assertThat(gameDTO.getBoard().getPlayedCards().isEmpty());
    }

    @Test
    void playCardFromHand() {
    }

    @Test
    void discardCardFromHand() {
    }

    @Test
    void activateProphecy() {
    }

    @Test
    void confirmProphecy() {
    }

    @Test
    void drawCardFromDeck() {
    }

    @Test
    void discardKeyCardFromHand() {
    }

    @Test
    void loseDoorCard() {
    }

    @Test
    void discardTopCardsFromDeck() {
    }

    @Test
    void discardPlayerHand() {
    }

}