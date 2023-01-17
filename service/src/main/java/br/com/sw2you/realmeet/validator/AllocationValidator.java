package br.com.sw2you.realmeet.validator;

import static br.com.sw2you.realmeet.validator.ValidatorConstants.*;

import java.time.Duration;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Component;
import br.com.sw2you.realmeet.api.model.CreateAllocationDTO;
import br.com.sw2you.realmeet.api.model.UpdateAllocationDTO;
import br.com.sw2you.realmeet.domain.repository.AllocationRepository;
import br.com.sw2you.realmeet.util.DateUtils;

@Component
public class AllocationValidator {
    private final AllocationRepository allocationRepository;

    public AllocationValidator(AllocationRepository allocationRepository) {
        this.allocationRepository = allocationRepository;
    }

    public void validate(CreateAllocationDTO createAllocationDTO) {
        var validatorErrors = new ValidationErrors();
        validateSubject(createAllocationDTO.getSubject(), validatorErrors);
        validateEmployeeName(createAllocationDTO.getEmployeeName(), validatorErrors);
        validateEmployeeEmail(createAllocationDTO.getEmployeeEmail(), validatorErrors);
        validateDates(createAllocationDTO.getStartAt(), createAllocationDTO.getEndAt(), validatorErrors);

        ValidatorUtils.throwOnError(validatorErrors);
    }

    public void validate(Long allocationId, UpdateAllocationDTO updateAllocationDTO) {
        var validatorErrors = new ValidationErrors();

        ValidatorUtils.validateRequired(allocationId, ALLOCATION_ID, validatorErrors);
        validateSubject(updateAllocationDTO.getSubject(), validatorErrors);
        validateDates(updateAllocationDTO.getStartAt(), updateAllocationDTO.getEndAt(), validatorErrors);

        ValidatorUtils.throwOnError(validatorErrors);
    }

    private void validateSubject(String subject, ValidationErrors validatorErrors) {
        ValidatorUtils.validateRequired(subject, ALLOCATION_SUBJECT, validatorErrors);
        ValidatorUtils.validateMaxLength(
            subject,
            ALLOCATION_SUBJECT,
            ValidatorConstants.ALLOCATION_SUBJECT_MAX_LENGTH,
            validatorErrors
        );
    }

    private void validateEmployeeName(String employeeName, ValidationErrors validatorErrors) {
        ValidatorUtils.validateRequired(employeeName, ALLOCATION_EMPLOYEE_NAME, validatorErrors);
        ValidatorUtils.validateMaxLength(
            employeeName,
            ALLOCATION_EMPLOYEE_NAME,
            ALLOCATION_EMPLOYEE_NAME_MAX_LENGTH,
            validatorErrors
        );
    }

    private void validateEmployeeEmail(String employeeEmail, ValidationErrors validatorErrors) {
        ValidatorUtils.validateRequired(employeeEmail, ALLOCATION_EMPLOYEE_EMAIL, validatorErrors);
        ValidatorUtils.validateMaxLength(
            employeeEmail,
            ALLOCATION_EMPLOYEE_EMAIL,
            ALLOCATION_EMPLOYEE_EMAIL_MAX_LENGTH,
            validatorErrors
        );
    }

    private void validateDates(OffsetDateTime startAt, OffsetDateTime endAt, ValidationErrors validationErrors) {
        if (validateDatePresent(startAt, endAt, validationErrors)) {
            validateDateOrdering(startAt, endAt, validationErrors);
            validateDateInTheFuture(startAt, validationErrors);
            validateDuration(startAt, endAt, validationErrors);
            validateIfTimeAvailable(startAt, endAt, validationErrors);
        }
    }

    private boolean validateDatePresent(
        OffsetDateTime startAt,
        OffsetDateTime endAt,
        ValidationErrors validationErrors
    ) {
        return (
            ValidatorUtils.validateRequired(startAt, ALLOCATION_START_AT, validationErrors) &&
            ValidatorUtils.validateRequired(endAt, ALLOCATION_END_AT, validationErrors)
        );
    }

    private void validateDateOrdering(OffsetDateTime startAt, OffsetDateTime endAt, ValidationErrors validationErrors) {
        if (startAt.isEqual(endAt) || startAt.isAfter(endAt)) {
            validationErrors.add(ALLOCATION_START_AT, ALLOCATION_START_AT + INCONSISTENT);
        }
    }

    private void validateDateInTheFuture(OffsetDateTime date, ValidationErrors validationErrors) {
        if (date.isBefore(DateUtils.now())) {
            validationErrors.add(ALLOCATION_START_AT, ALLOCATION_START_AT + IN_THE_PAST);
        }
    }

    private void validateDuration(OffsetDateTime startAt, OffsetDateTime endAt, ValidationErrors validationErrors) {
        if (Duration.between(startAt, endAt).getSeconds() > ALLOCATION_MAX_DURATION_SECONDS) {
            validationErrors.add(ALLOCATION_END_AT, ALLOCATION_END_AT + EXCEEDS_DURATION);
        }
    }

    private void validateIfTimeAvailable(
        OffsetDateTime startAt,
        OffsetDateTime endAt,
        ValidationErrors validationErrors
    ) {
        //todo

    }
}
