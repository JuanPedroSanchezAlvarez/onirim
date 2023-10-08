package com.misispiclix.singleplayergames.onirim.controller;

import com.misispiclix.singleplayergames.onirim.dto.GameDTO;
import com.misispiclix.singleplayergames.onirim.exception.GameNotFoundException;
import com.misispiclix.singleplayergames.onirim.service.IOnirimService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Onirim", description = "Onirim management APIs")
@Slf4j
@RequiredArgsConstructor
@RestController
public class OnirimRestController {

    public static final String ONIRIM_PATH = "/onirim/api";
    public static final String ONIRIM_PATH_ID = ONIRIM_PATH + "/{id}";
    //public static final String EXAMPLE_PATH = ONIRIM_PATH + "/example";
    //public static final String EXAMPLE_PATH_ID = EXAMPLE_PATH + "/{id}";

    @Qualifier(value = "onirimServiceImpl")
    private final IOnirimService onirimService;

    @Operation(summary = "Retrieve a Page of Games",
            description = "Get a page of game object. The response is a page object with a content of game objects.")
            //tags = { "game", "get" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)) }) })
    @GetMapping(path = ONIRIM_PATH)
    public ResponseEntity<Page<GameDTO>> getGames(@RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return new ResponseEntity<>(onirimService.getGames(pageNumber, pageSize), HttpStatus.OK);
    }

    /*@GetMapping(path = ONIRIM_PATH)
    public Page<GameDTO> getGames(@ParameterObject Pageable pageable) {
        return onirimService.getGames(pageable);
    }*/

    @Operation(summary = "Retrieve a Game by Id",
            description = "Get a game object by specifying its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GameDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Game not found.") })
    @GetMapping(path = ONIRIM_PATH_ID)
    public ResponseEntity<GameDTO> getGameById(@PathVariable(value = "id") UUID id) {
        return new ResponseEntity<>(onirimService.getGameById(id).orElseThrow(GameNotFoundException::new), HttpStatus.OK);
    }

    @Operation(summary = "Create a new Game",
            description = "Create a new game to start playing.")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Created") })
    @PostMapping(path = ONIRIM_PATH)
    public ResponseEntity<String> createNewGame() {
        UUID createdGameId = onirimService.createNewGame();
        String location = "Location: " + ONIRIM_PATH + "/" + createdGameId;
        return new ResponseEntity<>(location, HttpStatus.CREATED);
        //HttpHeaders headers = new HttpHeaders();
        //headers.add("Location", ONIRIM_PATH + "/" + createdGameId);
        //return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @Operation(summary = "Play a Card from Hand",
            description = "Play the selected card from the hand to the game.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Invalid card index."),
            @ApiResponse(responseCode = "400", description = "Not a Labyrinth Card."),
            @ApiResponse(responseCode = "400", description = "Equal Card Symbol."),
            @ApiResponse(responseCode = "404", description = "Game not found."),
            @ApiResponse(responseCode = "405", description = "Action not allowed.") })
    @PutMapping(path = ONIRIM_PATH_ID + "/playCardFromHand")
    public ResponseEntity<String> playCardFromHand(@PathVariable(value = "id") UUID id, @RequestBody Integer playedCardIndex) {
        onirimService.playCardFromHand(id, playedCardIndex);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Discard a Card from Hand",
            description = "Discard the selected card from the hand to the discard pile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Invalid card index."),
            @ApiResponse(responseCode = "404", description = "Game not found."),
            @ApiResponse(responseCode = "405", description = "Action not allowed.") })
    @PutMapping(path = ONIRIM_PATH_ID + "/discardCardFromHand")
    public ResponseEntity<String> discardCardFromHand(@PathVariable(value = "id") UUID id, @RequestBody Integer discardedCardIndex) {
        onirimService.discardCardFromHand(id, discardedCardIndex);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Activate a Prophecy",
            description = "Activate a prophecy from the card deck.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Game not found."),
            @ApiResponse(responseCode = "405", description = "Action not allowed.") })
    @PutMapping(path = ONIRIM_PATH_ID + "/activateProphecy")
    public ResponseEntity<String> activateProphecy(@PathVariable(value = "id") UUID id) {
        onirimService.activateProphecy(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Confirm a Prophecy",
            description = "Confirm a prophecy to the card deck.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Invalid card index."),
            @ApiResponse(responseCode = "404", description = "Game not found."),
            @ApiResponse(responseCode = "405", description = "Action not allowed.") })
    @PutMapping(path = ONIRIM_PATH_ID + "/confirmProphecy")
    public ResponseEntity<String> confirmProphecy(@PathVariable(value = "id") UUID id, @RequestBody Integer discardedCardIndex, @RequestBody List<Integer> reorderedCardIndexes) {
        onirimService.confirmProphecy(id, discardedCardIndex, reorderedCardIndexes);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Draw a Card from Deck",
            description = "Draw a new card from the card deck.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Game not found."),
            @ApiResponse(responseCode = "405", description = "Action not allowed.") })
    @PutMapping(path = ONIRIM_PATH_ID + "/drawCardFromDeck")
    public ResponseEntity<String> drawCardFromDeck(@PathVariable(value = "id") UUID id) {
        onirimService.drawCardFromDeck(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Discard a Key Card from Hand",
            description = "Discard the selected key card from the hand to the discard pile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Invalid card index."),
            @ApiResponse(responseCode = "400", description = "Not a Labyrinth Card."),
            @ApiResponse(responseCode = "400", description = "Not a Key Card."),
            @ApiResponse(responseCode = "404", description = "Game not found."),
            @ApiResponse(responseCode = "405", description = "Action not allowed.") })
    @PutMapping(path = ONIRIM_PATH_ID + "/discardKeyCardFromHand")
    public ResponseEntity<String> discardKeyCardFromHand(@PathVariable(value = "id") UUID id, @RequestBody Integer discardedCardIndex) {
        onirimService.discardKeyCardFromHand(id, discardedCardIndex);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Lose a Door Card",
            description = "Lose the selected door card from the discovered doors deck to the game deck.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Invalid card index."),
            @ApiResponse(responseCode = "404", description = "Game not found."),
            @ApiResponse(responseCode = "405", description = "Action not allowed.") })
    @PutMapping(path = ONIRIM_PATH_ID + "/loseDoorCard")
    public ResponseEntity<String> loseDoorCard(@PathVariable(value = "id") UUID id, @RequestBody Integer doorCardIndex) {
        onirimService.loseDoorCard(id, doorCardIndex);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Discard the top Cards from the Deck",
            description = "Discard the top 5 cards from the deck to the discard pile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Game not found."),
            @ApiResponse(responseCode = "405", description = "Action not allowed.") })
    @PutMapping(path = ONIRIM_PATH_ID + "/discardTopCardsFromDeck")
    public ResponseEntity<String> discardTopCardsFromDeck(@PathVariable(value = "id") UUID id) {
        onirimService.discardTopCardsFromDeck(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Discard the Player Hand",
            description = "Discard the entire player hand to the discard pile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Game not found."),
            @ApiResponse(responseCode = "405", description = "Action not allowed.") })
    @PutMapping(path = ONIRIM_PATH_ID + "/discardPlayerHand")
    public ResponseEntity<String> discardPlayerHand(@PathVariable(value = "id") UUID id) {
        onirimService.discardPlayerHand(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }




    /*@PostMapping(path = EXAMPLE_PATH)
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
    }*/

}
