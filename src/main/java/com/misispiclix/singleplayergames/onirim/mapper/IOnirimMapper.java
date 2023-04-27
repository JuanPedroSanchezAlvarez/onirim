package com.misispiclix.singleplayergames.onirim.mapper;

import com.misispiclix.singleplayergames.onirim.domain.Game;
import com.misispiclix.singleplayergames.onirim.domain.card.Card;
import com.misispiclix.singleplayergames.onirim.domain.card.DoorCard;
import com.misispiclix.singleplayergames.onirim.domain.card.LabyrinthCard;
import com.misispiclix.singleplayergames.onirim.domain.card.NightmareCard;
import com.misispiclix.singleplayergames.onirim.dto.GameDTO;
import com.misispiclix.singleplayergames.onirim.dto.card.CardDTO;
import com.misispiclix.singleplayergames.onirim.dto.card.DoorCardDTO;
import com.misispiclix.singleplayergames.onirim.dto.card.LabyrinthCardDTO;
import com.misispiclix.singleplayergames.onirim.dto.card.NightmareCardDTO;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

@Mapper(subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public interface IOnirimMapper {

    Game gameDtoToGame(GameDTO dto);
    GameDTO gameToGameDto(Game game);

    @SubclassMapping(source = LabyrinthCardDTO.class, target = LabyrinthCard.class)
    @SubclassMapping(source = DoorCardDTO.class, target = DoorCard.class)
    @SubclassMapping(source = NightmareCardDTO.class, target = NightmareCard.class)
    Card cardDtoToCard(CardDTO dto);
    @SubclassMapping(source = LabyrinthCard.class, target = LabyrinthCardDTO.class)
    @SubclassMapping(source = DoorCard.class, target = DoorCardDTO.class)
    @SubclassMapping(source = NightmareCard.class, target = NightmareCardDTO.class)
    CardDTO cardToCardDto(Card card);

}
