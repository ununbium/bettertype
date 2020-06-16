package dev.errant.bettertype.basic.failable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

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

}