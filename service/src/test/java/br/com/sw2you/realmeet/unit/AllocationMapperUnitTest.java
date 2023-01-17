package br.com.sw2you.realmeet.unit;

import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.domain.entity.Allocation;
import br.com.sw2you.realmeet.domain.entity.Room;
import br.com.sw2you.realmeet.mapper.AllocationMapper;
import br.com.sw2you.realmeet.mapper.RoomMapper;
import br.com.sw2you.realmeet.utils.MapperUtils;
import br.com.sw2you.realmeet.utils.TestConstants;
import br.com.sw2you.realmeet.utils.TestDataCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AllocationMapperUnitTest extends BaseUnitTest {
    private AllocationMapper victim;

    @BeforeEach
    void setupEach() {
        victim = MapperUtils.allocationMapper();
    }

    @Test
    void testFromCreateAllocationDTOToEntity() {
        var createAllocationDTO = TestDataCreator.newCreateAllocationDTO();
        var allocation = victim.fromCreateAllocationDTOToEntity(createAllocationDTO, Room.newBuilder().build());

        Assertions.assertEquals(createAllocationDTO.getSubject(), allocation.getSubject());
        Assertions.assertNull(allocation.getRoom().getId());
        Assertions.assertEquals(createAllocationDTO.getEmployeeName(), allocation.getEmployee().getName());
        Assertions.assertEquals(createAllocationDTO.getEmployeeEmail(), allocation.getEmployee().getEmail());
        Assertions.assertEquals(createAllocationDTO.getStartAt(), allocation.getStartAt());
        Assertions.assertEquals(createAllocationDTO.getEndAt(), allocation.getEndAt());
    }

    @Test
    void testFromEntityToAllocationDTO() {
        var allocation = TestDataCreator.newAllocationBuilder(Room.newBuilder().id(1L).build()).build();
        var allocationDTO = victim.fromEntityToAllocationDTO(allocation);

        Assertions.assertEquals(allocationDTO.getSubject(), allocationDTO.getSubject());
        Assertions.assertEquals(allocation.getId(), allocationDTO.getId());
        Assertions.assertNotNull(allocationDTO.getRoomId());
        Assertions.assertEquals(allocation.getRoom().getId(), allocationDTO.getRoomId());
        Assertions.assertEquals(allocation.getEmployee().getName(), allocationDTO.getEmployeeName());
        Assertions.assertEquals(allocation.getEmployee().getEmail(), allocationDTO.getEmployeeEmail());
        Assertions.assertEquals(allocation.getStartAt(), allocationDTO.getStartAt());
        Assertions.assertEquals(allocation.getEndAt(), allocationDTO.getEndAt());
    }
}
