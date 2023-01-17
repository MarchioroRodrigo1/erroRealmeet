package br.com.sw2you.realmeet.unit;

import static br.com.sw2you.realmeet.utils.TestDataCreator.newCreateAllocationDTO;
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

public class AllocationCreateValidatorUnitTest extends BaseUnitTest {
    private AllocationValidator victim;

    @Mock
    private AllocationRepository allocationRepository;

    @BeforeEach
    public void setupEach() {
        victim = new AllocationValidator(allocationRepository);
    }

    @Test
    public void testValidateWhenAllocationIsValid() {
        victim.validate(newCreateAllocationDTO());
    }

    @Test
    public void testValidateWhenSubjectNameIsMissing() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(newCreateAllocationDTO().subject(null))
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
                    newCreateAllocationDTO().subject(StringUtils.rightPad("X", ALLOCATION_SUBJECT_MAX_LENGTH + 1, 'X'))
                )
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_SUBJECT, ALLOCATION_SUBJECT + EXCEEDS_MAX_LENGTH),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    public void testValidateWhenEmployeeNameIsMissing() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(newCreateAllocationDTO().employeeName(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_EMPLOYEE_NAME, ALLOCATION_EMPLOYEE_NAME + MISSING),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    public void testValidateWhenEmployeeNameExceedsLength() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () ->
                victim.validate(
                    newCreateAllocationDTO()
                        .employeeName(StringUtils.rightPad("X", ALLOCATION_EMPLOYEE_NAME_MAX_LENGTH + 1, 'X'))
                )
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_EMPLOYEE_NAME, ALLOCATION_EMPLOYEE_NAME + EXCEEDS_MAX_LENGTH),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    public void testValidateWhenEmployeeEmailIsMissing() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(newCreateAllocationDTO().employeeEmail(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_EMPLOYEE_EMAIL, ALLOCATION_EMPLOYEE_EMAIL + MISSING),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    public void testValidateWhenEmployeeEmailExceedsLength() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () ->
                victim.validate(
                    newCreateAllocationDTO()
                        .employeeEmail(StringUtils.rightPad("X", ALLOCATION_EMPLOYEE_EMAIL_MAX_LENGTH + 1, 'X'))
                )
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_EMPLOYEE_EMAIL, ALLOCATION_EMPLOYEE_EMAIL + EXCEEDS_MAX_LENGTH),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    public void testValidateWhenStartAtIsMissing() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(newCreateAllocationDTO().startAt(null))
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
            () -> victim.validate(newCreateAllocationDTO().endAt(null))
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
                    newCreateAllocationDTO()
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
                    newCreateAllocationDTO()
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
                    newCreateAllocationDTO()
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
