package br.com.sw2you.realmeet.unit;

import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.mapper.RoomMapper;
import br.com.sw2you.realmeet.utils.MapperUtils;
import br.com.sw2you.realmeet.utils.TestConstants;
import br.com.sw2you.realmeet.utils.TestDataCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoomMapperUnitTest extends BaseUnitTest {
    private RoomMapper victim;

    @BeforeEach
    void setupEach() {
        victim = MapperUtils.roomMapper();
    }

    @Test
    void testFromEntityToDto() {
        var room = TestDataCreator.newRoomBuilder().id(TestConstants.DEFAULT_ROOM_ID).build();
        var dto = victim.fromEntityRoomToDto(room);
        Assertions.assertEquals(room.getId(), dto.getId());
        Assertions.assertEquals(room.getName(), dto.getName());
        Assertions.assertEquals(room.getSeats(), dto.getSeats());
    }

    @Test
    void testCreateRoomDtoToEntity() {
        var createRoomDTO = TestDataCreator.newCreateRooDTO();
        var room = victim.fromCreateRoomDtoToEntity(createRoomDTO);
        Assertions.assertEquals(createRoomDTO.getName(), room.getName());
        Assertions.assertEquals(createRoomDTO.getSeats(), room.getSeats());
    }
}
