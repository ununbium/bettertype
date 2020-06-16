package dev.errant.bettertype.basic.absorber;

import dev.errant.bettertype.basic.absorber.action.ActionThrowableAbsorber;
import dev.errant.bettertype.basic.failable.Failable;
import dev.errant.bettertype.basic.failable.SimpleFailable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ActionThrowableAbsorberTest {

    @Test
    @DisplayName("absorb the exception of a supplier action")
    public void absorbSupplierException() {
        //given
        Exception e = mock(Exception.class);

        //when
        SimpleFailable<Throwable> absorb = ActionThrowableAbsorber.absorb(() -> {
            throw e;
        });

        //then
        assertTrue(absorb.isFailure());
        assertEquals(e, absorb.getFailure());
     }

    @Test
    @DisplayName("absorb the success of a supplier action")
    public void absorbSupplierSuccess() {
        //given
        String successValue = "Success!";

        //when
        SimpleFailable<Throwable> absorb = ActionThrowableAbsorber.absorb(() -> {});

        //then
        assertTrue(absorb.isSuccess());
    }

    @Test
    @DisplayName("absorb the exception of an supplier action and convert it")
    public void absorbSupplierConverted() {
        //given
        Exception e = mock(Exception.class);
        String message = "bang";
        when(e.getMessage()).thenReturn(message);

        //when
        SimpleFailable<String> absorb = ActionThrowableAbsorber.absorb(() -> {
            throw e;
        }, Throwable::getMessage);

        //then
        assertTrue(absorb.isFailure());
        assertEquals(message, absorb.getFailure());
    }


}