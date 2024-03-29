package com.misispiclix.onirim.controller;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OnirimRestControllerITOld {

    /*@Autowired
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
    }*/

}