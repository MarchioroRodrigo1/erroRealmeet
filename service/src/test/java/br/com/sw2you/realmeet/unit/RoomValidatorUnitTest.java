package br.com.sw2you.realmeet.unit;

import static br.com.sw2you.realmeet.utils.TestDataCreator.newCreateRooDTO;
import static br.com.sw2you.realmeet.utils.TestDataCreator.newRoomBuilder;
import static br.com.sw2you.realmeet.validator.ValidatorConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.sw2you.realmeet.api.model.CreateRoomDTO;
import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.exception.InvalidRequestException;
import br.com.sw2you.realmeet.utils.TestConstants;
import br.com.sw2you.realmeet.validator.RoomValidator;
import br.com.sw2you.realmeet.validator.ValidationError;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;

public class RoomValidatorUnitTest extends BaseUnitTest {
    private RoomValidator victim;

    @Mock
    private RoomRepository roomRepository;

    @BeforeEach
    public void setupEach() {
        victim = new RoomValidator(roomRepository);
    }

    @Test
    public void testValidateWhenRoomIsValid() {
        victim.validate(newCreateRooDTO());
    }

    @Test
    public void testValidateWhenRoomNameIsMissing() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate((CreateRoomDTO) newCreateRooDTO().name(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ROOM_NAME, ROOM_NAME + MISSING), exception.getValidationErrors().getError(0));
    }

    @Test
    public void testValidateWhenRoomNameExceedsLength() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () ->
                victim.validate(
                    (CreateRoomDTO) newCreateRooDTO().name(StringUtils.rightPad("X", ROOM_NAME_MAX_LENGTH + 1, 'X'))
                )
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ROOM_NAME, ROOM_NAME + EXCEEDS_MAX_LENGTH),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    public void testValidateWhenRoomSeatAreMissing() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate((CreateRoomDTO) newCreateRooDTO().seats(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ROOM_SEATS, ROOM_SEATS + MISSING),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    public void testValidateWhenRoomSeatLessThanMinValue() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate((CreateRoomDTO) newCreateRooDTO().seats(ROOM_SEATS_MIN_VALUE - 1))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ROOM_SEATS, ROOM_SEATS + BELOW_MIN_VALUE),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    public void testValidateWhenRoomSeatGreatThanMaxValue() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate((CreateRoomDTO) newCreateRooDTO().seats(ROOM_SEATS_MAX_VALUE + 1))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ROOM_SEATS, ROOM_SEATS + EXCEEDS_MAX_VALUE),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenRoomNameIsDuplicate() {
        BDDMockito
            .given(roomRepository.findByNameAndActive(TestConstants.DEFAULT_ROOM_NAME, true))
            .willReturn(Optional.of(newRoomBuilder().build()));
        var exception = assertThrows(InvalidRequestException.class, () -> victim.validate(newCreateRooDTO()));
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ROOM_NAME, ROOM_NAME + DUPLICATE),
            exception.getValidationErrors().getError(0)
        );
    }
}
