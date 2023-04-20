package com.misispiclix.singleplayergames.onirim.controller;

import com.misispiclix.singleplayergames.onirim.domain.Game;
import com.misispiclix.singleplayergames.onirim.service.IOnirimService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/onirim/api")
public class OnirimRestController {

    @Qualifier(value = "onirimServiceImpl")
    private final IOnirimService onirimService;

    @GetMapping(path = "/example")
    public Iterable<Game> getExamples() {
        return onirimService.getExamples();
    }

    @GetMapping(path = "/example/{id}")
    public Game getExampleById(@PathVariable(value = "id") Long id) {
        return onirimService.getExampleById(id);
    }

    @PostMapping(path = "/example")
    public ResponseEntity createExample(@RequestBody Game game) {
        Game createdGame = onirimService.createExample(game);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/onirim/api/example/" + createdGame.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(path = "/example/{id}")
    public ResponseEntity updateExample(@PathVariable(value = "id") Long id, @RequestBody Game game) {
        onirimService.updateExample(id, game);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = "/example/{id}")
    public ResponseEntity updateExamplePatch(@PathVariable(value = "id") Long id, @RequestBody Game game) {
        onirimService.updateExamplePatch(id, game);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/example/{id}")
    public ResponseEntity deleteExample(@PathVariable(value = "id") Long id) {
        onirimService.deleteExample(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
