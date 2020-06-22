package dev.errant.bettertype.basic.failable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleFailableTest {

    @Test
    @DisplayName("basic usage success")
    public void basicUsageSuccess() {
        //given
        SimpleFailable<Integer> httpResponse;

        //when
        httpResponse = SimpleFailable.success();

        //then
        assertTrue(httpResponse.isSuccess());
        assertFalse(httpResponse.isFailure());
     }

    @Test
    @DisplayName("basic usage failure")
    public void basicUsageFailure() {
        //given
        SimpleFailable<List<String>> httpResponse;
        List<String> rawFailureValue = mock(List.class);

        //when
        httpResponse = SimpleFailable.failure(rawFailureValue);

        //then
        assertTrue(httpResponse.isFailure());
        assertFalse(httpResponse.isSuccess());
        assertEquals(rawFailureValue, httpResponse.getFailure());
    }

    @Test
    @DisplayName("illegal usage success")
    public void illegalUsageSuccess() {
        //given
        SimpleFailable<Integer> httpResponse = SimpleFailable.success();

        //when
        assertThrows(NoSuchElementException.class, httpResponse::getFailure);

        //then
    }

    @Test
    @DisplayName("illegal null construction - failure case")
    public void illegalNullConstructionFailure() {
        //given

        //when
        assertThrows(AssertionError.class, () -> {
            SimpleFailable.failure(null);
        });

        //then
    }


    @Test
    @DisplayName("absorb the exception of a supplier action")
    public void absorbSupplierException() {
        //given
        Exception e = mock(Exception.class);

        //when
        SimpleFailable<Exception> absorb = SimpleFailable.absorb(() -> {
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
        SimpleFailable<Exception> absorb = SimpleFailable.absorb(() -> {});

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
        SimpleFailable<String> absorb = SimpleFailable.absorb(() -> {
            throw e;
        }, Exception::getMessage);

        //then
        assertTrue(absorb.isFailure());
        assertEquals(message, absorb.getFailure());
    }

}