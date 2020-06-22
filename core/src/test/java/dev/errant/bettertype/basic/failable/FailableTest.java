package dev.errant.bettertype.basic.failable;

import dev.errant.bettertype.basic.absorber.NullValueAbsorbedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FailableTest {

    @Test
    @DisplayName("basic usage success")
    public void basicUsageSuccess() {
        //given
        Failable<String, Integer> httpResponse;
        String rawSuccessValue = "some string";

        //when
        httpResponse = Failable.success(rawSuccessValue);

        //then
        assertTrue(httpResponse.isSuccess());
        assertFalse(httpResponse.isFailure());
        assertEquals(rawSuccessValue, httpResponse.getSuccess());
     }

    @Test
    @DisplayName("basic usage failure")
    public void basicUsageFailure() {
        //given
        Failable<String, List<String>> httpResponse;
        List<String> rawFailureValue = mock(List.class);

        //when
        httpResponse = Failable.failure(rawFailureValue);

        //then
        assertTrue(httpResponse.isFailure());
        assertFalse(httpResponse.isSuccess());
        assertEquals(rawFailureValue, httpResponse.getFailure());
    }

    @Test
    @DisplayName("illegal usage success")
    public void illegalUsageSuccess() {
        //given
        Failable<String, Integer> httpResponse;
        String rawSuccessValue = "some string";
        httpResponse = Failable.success(rawSuccessValue);

        //when
        assertThrows(NoSuchElementException.class, httpResponse::getFailure);

        //then
    }

    @Test
    @DisplayName("illegal usage failure")
    public void illegalUsageFailure() {
        //given
        Failable<String, Integer> httpResponse;
        Integer rawFailureValue = 7;
        httpResponse = Failable.failure(rawFailureValue);

        //when
        assertThrows(NoSuchElementException.class, httpResponse::getSuccess);

        //then
    }

    @Test
    @DisplayName("illegal null construction - success case")
    public void illegalNullConstructionSuccess() {
        //given

        //when
        assertThrows(AssertionError.class, () -> {
            Failable.success(null);
        });

        //then
    }

    @Test
    @DisplayName("illegal null construction - failure case")
    public void illegalNullConstructionFailure() {
        //given

        //when
        assertThrows(AssertionError.class, () -> {
            Failable.failure(null);
        });

        //then
    }


    @Test
    @DisplayName("absorb the exception of a supplier action")
    public void absorbException() {
        //given
        Exception e = mock(Exception.class);

        //when
        Failable<Object, Throwable> absorb = Failable.absorb(() -> {
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
        Failable<String, Throwable> absorb = Failable.absorb(() -> successValue);

        //then
        assertTrue(absorb.isSuccess());
        assertEquals(successValue, absorb.getSuccess());
    }

    @Test
    @DisplayName("absorb the exception of a supplier action and convert it")
    public void absorbError() {
        //given
        Exception e = mock(Exception.class);
        String message = "bang";
        when(e.getMessage()).thenReturn(message);

        //when
        Failable<Integer, String> absorb = Failable.absorb(() -> {
            throw e;
        }, Throwable::getMessage);

        //then
        assertTrue(absorb.isFailure());
        assertEquals(message, absorb.getFailure());
    }

    @Test
    @DisplayName("absorb the exception of supplier action and convert it")
    public void absorbNull() {
        //given
        Exception e = mock(Exception.class);
        String message = "bang";
        when(e.getMessage()).thenReturn(message);

        //when
        Failable<Integer, Throwable> absorb = Failable.absorb(
                () -> null,
                (t)->t);

        //then
        assertTrue(absorb.isFailure());
        assertEquals(NullValueAbsorbedException.class, absorb.getFailure().getClass());
    }

}
