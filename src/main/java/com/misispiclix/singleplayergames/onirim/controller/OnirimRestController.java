package com.misispiclix.singleplayergames.onirim.controller;

import com.misispiclix.singleplayergames.onirim.domain.Game;
import com.misispiclix.singleplayergames.onirim.dto.GameDTO;
import com.misispiclix.singleplayergames.onirim.exception.NotFoundException;
import com.misispiclix.singleplayergames.onirim.service.IOnirimService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(path = EXAMPLE_PATH)
    public List<GameDTO> getExamples() {
        return onirimService.getExamples();
    }

    @GetMapping(path = EXAMPLE_PATH_ID)
    public GameDTO getExampleById(@PathVariable(value = "id") UUID id) {
        return onirimService.getExampleById(id).orElseThrow(NotFoundException::new);
    }

    @PostMapping(path = EXAMPLE_PATH)
    public ResponseEntity createExample(@RequestBody GameDTO gameDTO) {
        GameDTO createdGame = onirimService.createExample(gameDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", EXAMPLE_PATH + createdGame.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(path = EXAMPLE_PATH_ID)
    public ResponseEntity updateExample(@PathVariable(value = "id") UUID id, @RequestBody GameDTO gameDTO) {
        if (onirimService.updateExample(id, gameDTO).isEmpty()) {
            throw new NotFoundException();
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
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
