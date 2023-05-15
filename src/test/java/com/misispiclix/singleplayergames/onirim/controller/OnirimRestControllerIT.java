package com.misispiclix.singleplayergames.onirim.controller;

import com.misispiclix.singleplayergames.onirim.domain.Game;
import com.misispiclix.singleplayergames.onirim.dto.GameDTO;
import com.misispiclix.singleplayergames.onirim.exception.GameNotFoundException;
import com.misispiclix.singleplayergames.onirim.mapper.IOnirimMapper;
import com.misispiclix.singleplayergames.onirim.repository.IOnirimRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class OnirimRestControllerIT {

    @Autowired
    OnirimRestController onirimRestController;

    @Autowired
    IOnirimRepository onirimRepository;

    @Autowired
    IOnirimMapper onirimMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getExamples() {
        List<GameDTO> listOfGameDto = onirimRestController.getExamples();
        assertThat(listOfGameDto.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        onirimRepository.deleteAll();
        List<GameDTO> listOfGameDto = onirimRestController.getExamples();
        assertThat(listOfGameDto.size()).isEqualTo(0);
    }

    @Test
    void getExampleById() {
        Game game = onirimRepository.findAll().get(0);
        GameDTO dto = onirimRestController.getExampleById(game.getId());
        assertThat(dto).isNotNull();
    }

    @Test
    void getExampleByIdNotFound() {
        assertThrows(GameNotFoundException.class, () -> {
            onirimRestController.getExampleById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void createExample() {
        GameDTO dto = new GameDTO();
        dto.setMessageToDisplay("Create Test 1");
        ResponseEntity responseEntity = onirimRestController.createExample(dto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);
        Game game = onirimRepository.findById(savedUUID).get();
        assertThat(game).isNotNull();
    }

    @Rollback
    @Transactional
    @Test
    void updateExample() {
        Game game = onirimRepository.findAll().get(0);
        GameDTO dto = onirimMapper.gameToGameDto(game);
        dto.setId(null);
        dto.setVersion(null);
        final String messageToDisplay = "UPDATED";
        dto.setMessageToDisplay(messageToDisplay);
        ResponseEntity responseEntity = onirimRestController.updateExample(game.getId(), dto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        Game updatedGame = onirimRepository.findById(game.getId()).get();
        assertThat(updatedGame.getMessageToDisplay()).isEqualTo(messageToDisplay);
    }

    @Test
    void updateExampleNotFound() {
        assertThrows(GameNotFoundException.class, () -> {
            onirimRestController.updateExample(UUID.randomUUID(), new GameDTO());
        });
    }

    @Test
    void updateExamplePatch() {
    }

    @Rollback
    @Transactional
    @Test
    void deleteExample() {
        Game game = onirimRepository.findAll().get(0);
        ResponseEntity responseEntity = onirimRestController.deleteExample(game.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(onirimRepository.findById(game.getId()).isEmpty());
    }

    @Test
    void deleteExampleNotFound() {
        assertThrows(GameNotFoundException.class, () -> {
            onirimRestController.deleteExample(UUID.randomUUID());
        });
    }

}