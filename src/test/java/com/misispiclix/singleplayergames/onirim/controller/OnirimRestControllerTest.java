package com.misispiclix.singleplayergames.onirim.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.misispiclix.singleplayergames.onirim.domain.Board;
import com.misispiclix.singleplayergames.onirim.domain.Game;
import com.misispiclix.singleplayergames.onirim.domain.card.DoorCard;
import com.misispiclix.singleplayergames.onirim.domain.card.LabyrinthCard;
import com.misispiclix.singleplayergames.onirim.domain.card.NightmareCard;
import com.misispiclix.singleplayergames.onirim.enums.AllowedAction;
import com.misispiclix.singleplayergames.onirim.enums.Color;
import com.misispiclix.singleplayergames.onirim.enums.Symbol;
import com.misispiclix.singleplayergames.onirim.service.IOnirimService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static com.misispiclix.singleplayergames.onirim.controller.OnirimRestController.EXAMPLE_PATH;
import static com.misispiclix.singleplayergames.onirim.controller.OnirimRestController.EXAMPLE_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OnirimRestController.class)
class OnirimRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<UUID> idArgumentCaptor;

    @Captor
    ArgumentCaptor<Game> gameArgumentCaptor;

    @MockBean
    @Qualifier(value = "onirimServiceImpl")
    private IOnirimService onirimService;

    private Game game;

    @BeforeEach
    void setUp() {
        Game game = new Game();
        Board board = new Board();
        board.setCardDeck(new ArrayList<>());
        board.setDiscardedCards(new ArrayList<>());
        board.setLimboStack(new ArrayList<>());
        board.setCardsToShow(new ArrayList<>());
        board.setPlayedCards(new ArrayList<>());
        board.setDiscoveredDoors(new ArrayList<>());
        board.setPlayerHand(new ArrayList<>());

        DoorCard doorCard1 = new DoorCard();
        doorCard1.setColor(Color.BLUE);
        DoorCard doorCard2 = new DoorCard();
        doorCard2.setColor(Color.RED);
        DoorCard doorCard3 = new DoorCard();
        doorCard3.setColor(Color.GREEN);
        DoorCard doorCard4 = new DoorCard();
        doorCard4.setColor(Color.YELLOW);

        LabyrinthCard labyrinthCard1 = new LabyrinthCard();
        labyrinthCard1.setColor(Color.BLUE);
        labyrinthCard1.setSymbol(Symbol.KEY);
        LabyrinthCard labyrinthCard2 = new LabyrinthCard();
        labyrinthCard2.setColor(Color.YELLOW);
        labyrinthCard2.setSymbol(Symbol.SUN);
        LabyrinthCard labyrinthCard3 = new LabyrinthCard();
        labyrinthCard3.setColor(Color.GREEN);
        labyrinthCard3.setSymbol(Symbol.MOON);
        LabyrinthCard labyrinthCard4 = new LabyrinthCard();
        labyrinthCard4.setColor(Color.RED);
        labyrinthCard4.setSymbol(Symbol.KEY);
        LabyrinthCard labyrinthCard5 = new LabyrinthCard();
        labyrinthCard5.setColor(Color.BLUE);
        labyrinthCard5.setSymbol(Symbol.KEY);
        LabyrinthCard labyrinthCard6 = new LabyrinthCard();
        labyrinthCard6.setColor(Color.YELLOW);
        labyrinthCard6.setSymbol(Symbol.SUN);
        LabyrinthCard labyrinthCard7 = new LabyrinthCard();
        labyrinthCard7.setColor(Color.GREEN);
        labyrinthCard7.setSymbol(Symbol.MOON);
        LabyrinthCard labyrinthCard8 = new LabyrinthCard();
        labyrinthCard8.setColor(Color.RED);
        labyrinthCard8.setSymbol(Symbol.KEY);

        NightmareCard nightmareCard1 = new NightmareCard();
        NightmareCard nightmareCard2 = new NightmareCard();

        board.getCardDeck().add(nightmareCard1);
        board.getCardDeck().add(doorCard1);
        board.getDiscardedCards().add(nightmareCard2);
        board.getDiscardedCards().add(labyrinthCard1);
        board.getLimboStack().add(doorCard2);
        board.getLimboStack().add(labyrinthCard2);
        board.getCardsToShow().add(labyrinthCard3);
        board.getCardsToShow().add(labyrinthCard4);
        board.getPlayedCards().add(labyrinthCard5);
        board.getPlayedCards().add(labyrinthCard6);
        board.getPlayerHand().add(labyrinthCard7);
        board.getPlayerHand().add(labyrinthCard8);
        board.getDiscoveredDoors().add(doorCard3);
        board.getDiscoveredDoors().add(doorCard4);

        game.setBoard(board);
        game.setAllowedActions(List.of(AllowedAction.PLAY_CARD_FROM_HAND, AllowedAction.DISCARD_CARD_FROM_HAND));
        game.setMessageToDisplay("Hello I am game 1");

        this.game = game;
    }

    @Test
    void getExamples() throws Exception {
        mockMvc.perform(get(EXAMPLE_PATH).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void getExampleById() throws Exception {
        mockMvc.perform(get(EXAMPLE_PATH_ID, "1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("1")));
    }

    @Test
    void getExampleByIdNotFoundException() throws Exception {
        mockMvc.perform(get(EXAMPLE_PATH_ID, "999")).andExpect(status().isNotFound());
    }

    @Test
    void createExample() throws Exception {
        mockMvc.perform(post(EXAMPLE_PATH).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(this.game)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void updateExample() throws Exception {
        this.game.setId(UUID.fromString("1"));
        mockMvc.perform(put(EXAMPLE_PATH_ID, "1").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(this.game)))
                .andExpect(status().isNoContent());
        //verify(onirimService).updateExample(any(UUID.class), any(Game.class));
    }

    @Test
    void updateExamplePatch() throws Exception {
        Map<String, Object> gameMap = new HashMap<>();
        gameMap.put("messageToDisplay", "Hola 2");
        mockMvc.perform(patch(EXAMPLE_PATH_ID, "1").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameMap)))
                .andExpect(status().isNoContent());
        //verify(onirimService).updateExamplePatch(idArgumentCaptor.getValue(), gameArgumentCaptor.getValue());
        assertThat("1").isEqualTo(idArgumentCaptor.getValue());
        assertThat(gameMap.get("messageToDisplay")).isEqualTo(gameArgumentCaptor.getValue().getMessageToDisplay());
    }

    @Test
    void deleteExample() throws Exception {
        mockMvc.perform(delete(EXAMPLE_PATH_ID, "1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(onirimService).deleteExample(idArgumentCaptor.capture());
        assertThat("1").isEqualTo(idArgumentCaptor.getValue());
    }

}