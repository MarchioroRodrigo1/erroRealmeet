package br.com.sw2you.realmeet.utils;

import br.com.sw2you.realmeet.api.model.CreateAllocationDTO;
import br.com.sw2you.realmeet.api.model.CreateRoomDTO;
import br.com.sw2you.realmeet.api.model.UpdateAllocationDTO;
import br.com.sw2you.realmeet.domain.entity.Allocation;
import br.com.sw2you.realmeet.domain.entity.Room;
import br.com.sw2you.realmeet.domain.model.Employee;

public final class TestDataCreator {

    public TestDataCreator() {}

    public static Room.Builder newRoomBuilder() {
        return Room.newBuilder().name(TestConstants.DEFAULT_ROOM_NAME).seats(TestConstants.DEFAULT_ROOM_SEATS);
    }

    public static Allocation.Builder newAllocationBuilder(Room room) {
        return Allocation
            .newBuilder()
            .subject(TestConstants.DEFAULT_ALLOCATION_SUBJECT)
            .room(room)
            .employee(
                Employee
                    .newBuilder()
                    .name(TestConstants.DEFAULT_EMPLOYEE_NAME)
                    .email(TestConstants.DEFAULT_EMPLOYEE_EMAIL)
                    .build()
            )
            .startAt(TestConstants.DEFAULT_ALLOCATION_START_AT)
            .endAt(TestConstants.DEFAULT_ALLOCATION_END_AT);
    }

    public static CreateRoomDTO newCreateRooDTO() {
        return (CreateRoomDTO) new CreateRoomDTO()
            .name(TestConstants.DEFAULT_ROOM_NAME)
            .seats(TestConstants.DEFAULT_ROOM_SEATS);
    }

    public static CreateAllocationDTO newCreateAllocationDTO() {
        return new CreateAllocationDTO()
            .subject(TestConstants.DEFAULT_ALLOCATION_SUBJECT)
            .roomId(TestConstants.DEFAULT_ROOM_ID)
            .employeeName(TestConstants.DEFAULT_EMPLOYEE_NAME)
            .employeeEmail(TestConstants.DEFAULT_EMPLOYEE_EMAIL)
            .startAt(TestConstants.DEFAULT_ALLOCATION_START_AT)
            .endAt(TestConstants.DEFAULT_ALLOCATION_END_AT);
    }

    public static UpdateAllocationDTO newUpdateAllocationDTO() {
        return new UpdateAllocationDTO()
            .subject(TestConstants.DEFAULT_ALLOCATION_SUBJECT)
            .startAt(TestConstants.DEFAULT_ALLOCATION_START_AT)
            .endAt(TestConstants.DEFAULT_ALLOCATION_END_AT);
    }
}
