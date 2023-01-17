package br.com.sw2you.realmeet.validator;

import static br.com.sw2you.realmeet.validator.ValidatorConstants.*;

import br.com.sw2you.realmeet.api.model.CreateRoomDTO;
import br.com.sw2you.realmeet.api.model.UpdateRoomDTO;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class RoomValidator {
    private final RoomRepository roomRepository;

    public RoomValidator(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void validate(CreateRoomDTO createRoomDTO) {
        var validatorErrors = new ValidationErrors();

        if (
            validateName(createRoomDTO.getName(), validatorErrors) &&
            validateSeats(createRoomDTO.getSeats(), validatorErrors)
        ) {
            validateNomeDuplicate(null, createRoomDTO.getName(), validatorErrors);
        }
        ValidatorUtils.throwOnError(validatorErrors);
    }

    public void validate(Long roomId, UpdateRoomDTO updateRoomDTO) {
        var validatorErrors = new ValidationErrors();

        if (
            ValidatorUtils.validateRequired(roomId, ROOM_ID, validatorErrors) &&
            validateName(updateRoomDTO.getName(), validatorErrors) &&
            validateSeats(updateRoomDTO.getSeats(), validatorErrors)
        ) {
            validateNomeDuplicate(roomId, updateRoomDTO.getName(), validatorErrors);
        }
        ValidatorUtils.throwOnError(validatorErrors);
    }

    private boolean validateName(String name, ValidationErrors validatorErrors) {
        return (
            ValidatorUtils.validateRequired(name, ROOM_NAME, validatorErrors) &&
            ValidatorUtils.validateMaxLength(name, ROOM_NAME, ValidatorConstants.ROOM_NAME_MAX_LENGTH, validatorErrors)
        );
    }

    private boolean validateSeats(Integer seats, ValidationErrors validatorErrors) {
        return (
            ValidatorUtils.validateRequired(seats, ValidatorConstants.ROOM_SEATS, validatorErrors) &&
            ValidatorUtils.validateMinValue(
                seats,
                ValidatorConstants.ROOM_SEATS,
                ValidatorConstants.ROOM_SEATS_MIN_VALUE,
                validatorErrors
            ) &&
            ValidatorUtils.validateMaxValue(
                seats,
                ValidatorConstants.ROOM_SEATS,
                ValidatorConstants.ROOM_SEATS_MAX_VALUE,
                validatorErrors
            )
        );
    }

    private void validateNomeDuplicate(Long roomIdToExclude, String nome, ValidationErrors validatorErrors) {
        roomRepository
            .findByNameAndActive(nome, true)
            .ifPresent(
                room -> {
                    if (Objects.isNull(roomIdToExclude) || !Objects.equals(room.getId(), roomIdToExclude)) {
                        validatorErrors.add(ROOM_NAME, ROOM_NAME + DUPLICATE);
                    }
                }
            );
    }
}
