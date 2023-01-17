package br.com.sw2you.realmeet.unit;

import static br.com.sw2you.realmeet.utils.TestConstants.DEFAULT_ALLOCATION_ID;
import static br.com.sw2you.realmeet.utils.TestDataCreator.newUpdateAllocationDTO;
import static br.com.sw2you.realmeet.validator.ValidatorConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.domain.repository.AllocationRepository;
import br.com.sw2you.realmeet.exception.InvalidRequestException;
import br.com.sw2you.realmeet.util.DateUtils;
import br.com.sw2you.realmeet.validator.AllocationValidator;
import br.com.sw2you.realmeet.validator.ValidationError;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class AllocationUpdateValidatorUnitTest extends BaseUnitTest {
    private AllocationValidator victim;

    @Mock
    private AllocationRepository allocationRepository;

    @BeforeEach
    public void setupEach() {
        victim = new AllocationValidator(allocationRepository);
    }

    @Test
    public void testValidateWhenAllocationIsValid() {
        victim.validate(DEFAULT_ALLOCATION_ID, newUpdateAllocationDTO());
    }

    @Test
    public void testValidateWhenAllocationIdIsMissing() {
        var exception = Assertions.assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(null ,newUpdateAllocationDTO())
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
                new ValidationError(ALLOCATION_ID, ALLOCATION_ID + MISSING),
                exception.getValidationErrors().getError(0)
        );
    }
    @Test
    public void testValidateWhenSubjectIsMissing() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(DEFAULT_ALLOCATION_ID ,newUpdateAllocationDTO().subject(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_SUBJECT, ALLOCATION_SUBJECT + MISSING),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    public void testValidateWhenSubjectExceedsLength() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () ->
                victim.validate(
                    DEFAULT_ALLOCATION_ID,
                    newUpdateAllocationDTO().subject(StringUtils.rightPad("X", ALLOCATION_SUBJECT_MAX_LENGTH + 1, 'X'))
                )
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_SUBJECT, ALLOCATION_SUBJECT + EXCEEDS_MAX_LENGTH),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    public void testValidateWhenStartAtIsMissing() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(DEFAULT_ALLOCATION_ID, newUpdateAllocationDTO().startAt(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_START_AT, ALLOCATION_START_AT + MISSING),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    public void testValidateWhenEndAtIsMissing() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(DEFAULT_ALLOCATION_ID, newUpdateAllocationDTO().endAt(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_END_AT, ALLOCATION_END_AT + MISSING),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    public void testValidateWhenDateOrderIsInvalid() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () ->
                victim.validate(
                    DEFAULT_ALLOCATION_ID,
                    newUpdateAllocationDTO()
                    .startAt(DateUtils.now().plusDays(1))
                    .endAt(DateUtils.now().plusDays(1).minusMinutes(30))
                )
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_START_AT, ALLOCATION_START_AT + INCONSISTENT),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    public void testValidateWhenDateIsThePast() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () ->
                victim.validate(
                    DEFAULT_ALLOCATION_ID,
                    newUpdateAllocationDTO()
                        .startAt(DateUtils.now().minusMinutes(30))
                        .endAt(DateUtils.now().plusMinutes(30))
                )
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_START_AT, ALLOCATION_START_AT + IN_THE_PAST),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    public void testValidateWhenDateIntervalExceedsDuration() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () ->
                victim.validate(
                    DEFAULT_ALLOCATION_ID,
                    newUpdateAllocationDTO()
                        .startAt(DateUtils.now().plusDays(1))
                        .endAt(DateUtils.now().plusDays(1).plusSeconds(ALLOCATION_MAX_DURATION_SECONDS + 1))
                )
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_END_AT, ALLOCATION_END_AT + EXCEEDS_DURATION),
            exception.getValidationErrors().getError(0)
        );
    }
}
