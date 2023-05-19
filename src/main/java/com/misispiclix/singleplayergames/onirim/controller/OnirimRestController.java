package com.misispiclix.singleplayergames.onirim.controller;

import com.misispiclix.singleplayergames.onirim.dto.GameDTO;
import com.misispiclix.singleplayergames.onirim.exception.GameNotFoundException;
import com.misispiclix.singleplayergames.onirim.service.IOnirimService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OnirimRestController {

    public static final String ONIRIM_PATH = "/onirim/api";
    public static final String ONIRIM_PATH_ID = ONIRIM_PATH + "/{id}";
    public static final String EXAMPLE_PATH = ONIRIM_PATH + "/example";
    public static final String EXAMPLE_PATH_ID = EXAMPLE_PATH + "/{id}";

    @Qualifier(value = "onirimServiceImpl")
    private final IOnirimService onirimService;

    @GetMapping(path = ONIRIM_PATH)
    public Page<GameDTO> getGames(@RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return onirimService.getGames(pageNumber, pageSize);
    }

    @GetMapping(path = ONIRIM_PATH_ID)
    public GameDTO getGameById(@PathVariable(value = "id") UUID id) {
        return onirimService.getGameById(id).orElseThrow(GameNotFoundException::new);
    }

    @PostMapping(path = ONIRIM_PATH)
    public ResponseEntity createNewGame() {
        UUID createdGameId = onirimService.createNewGame();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", ONIRIM_PATH + "/" + createdGameId);
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(path = ONIRIM_PATH_ID + "/playCardFromHand")
    public ResponseEntity playCardFromHand(@PathVariable(value = "id") UUID id, @RequestBody Integer playedCardIndex) {
        onirimService.playCardFromHand(id, playedCardIndex);
        return new ResponseEntity(HttpStatus.OK);
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

    @PutMapping(path = ONIRIM_PATH_ID + "/drawCardFromDeck")
    public ResponseEntity drawCardFromDeck(@PathVariable(value = "id") UUID id) {
        onirimService.drawCardFromDeck(id);
        return new ResponseEntity(HttpStatus.OK);
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




    @PostMapping(path = EXAMPLE_PATH)
    public ResponseEntity createExample(@Validated @RequestBody GameDTO gameDTO) {
        GameDTO createdGame = onirimService.createExample(gameDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", EXAMPLE_PATH + createdGame.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(path = EXAMPLE_PATH_ID)
    public ResponseEntity updateExample(@PathVariable(value = "id") UUID id, @Validated @RequestBody GameDTO gameDTO) {
        if (onirimService.updateExample(id, gameDTO).isEmpty()) {
            throw new GameNotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = EXAMPLE_PATH_ID)
    public ResponseEntity updateExamplePatch(@PathVariable(value = "id") UUID id, @RequestBody GameDTO gameDTO) {
        onirimService.updateExamplePatch(id, gameDTO);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = EXAMPLE_PATH_ID)
    public ResponseEntity deleteExample(@PathVariable(value = "id") UUID id) {
        if (!onirimService.deleteExample(id)) {
            throw new GameNotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
