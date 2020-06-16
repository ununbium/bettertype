package dev.errant.bettertype.basic.absorber;

import dev.errant.bettertype.basic.absorber.supplier.SupplierThrowableAbsorber;
import dev.errant.bettertype.basic.failable.Failable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SupplierThrowableAbsorberTest {

    @Test
    @DisplayName("absorb the exception of a supplier action")
    public void absorbException() {
        //given
        Exception e = mock(Exception.class);

        //when
        Failable<Object, Throwable> absorb = SupplierThrowableAbsorber.absorb(() -> {
            throw e;
        });

        //then
        assertTrue(absorb.isFailure());
        assertEquals(e, absorb.getFailure());
    }

    @Test
    @DisplayName("absorb the success of an action")
    public void absorbSuccess() {
        //given
        String successValue = "Success!";

        //when
        Failable<String, Throwable> absorb = SupplierThrowableAbsorber.absorb(() -> successValue);

        //then
        assertTrue(absorb.isSuccess());
        assertEquals(successValue, absorb.getSuccess());
    }

    @Test
    @DisplayName("absorb the exception of a supplier action and convert it")
    public void absorbConverted() {
        //given
        Exception e = mock(Exception.class);
        String message = "bang";
        when(e.getMessage()).thenReturn(message);

        //when
        Failable<Integer, String> absorb = SupplierThrowableAbsorber.absorb(() -> {
            throw e;
        }, Throwable::getMessage);

        //then
        assertTrue(absorb.isFailure());
        assertEquals(message, absorb.getFailure());
    }
}
