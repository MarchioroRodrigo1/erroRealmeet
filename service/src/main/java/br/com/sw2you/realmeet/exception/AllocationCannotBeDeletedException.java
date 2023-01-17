package br.com.sw2you.realmeet.exception;

import br.com.sw2you.realmeet.validator.ValidationError;
import br.com.sw2you.realmeet.validator.ValidatorConstants;

public class AllocationCannotBeDeletedException extends InvalidRequestException {

    public AllocationCannotBeDeletedException() {
        super(
            new ValidationError(
                ValidatorConstants.ALLOCATION_ID,
                ValidatorConstants.ALLOCATION_ID + ValidatorConstants.IN_THE_PAST
            )
        );
    }
}
