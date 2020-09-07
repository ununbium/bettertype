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

    @Test
    @DisplayName("map to a new type - failure value is mapped to same type")
    public void mapToNewFailure() {
        //given
        SimpleFailable<String> source = SimpleFailable.failure("some message");

        //when
        SimpleFailable<String> mapped = source.mapFailure(a -> a + " additional text");


        //then
        assertTrue(mapped.isFailure());
        assertEquals("some message additional text", mapped.getFailure());
    }


    @Test
    @DisplayName("map to a new type - failure value is mapped to different type")
    public void mapToNewFailureWithDifferentType() {
        //given
        SimpleFailable<String> source = SimpleFailable.failure("some message");

        //when
        SimpleFailable<Integer> mapped = source.mapFailure(a -> a.length());

        //then
        assertTrue(mapped.isFailure());
        assertEquals(12, mapped.getFailure());
    }

    @Test
    @DisplayName("map to a new type - success")
    public void mapToNewFailureWithDifferentTypeSuccess() {
        //given
        SimpleFailable<String> source = SimpleFailable.success();

        //when
        SimpleFailable<Integer> mapped = source.mapFailure(a -> a.length());

        //then
        assertTrue(mapped.isSuccess());
    }

    @Test
    @DisplayName("map to a new type - null mapping should throw an exception on failure")
    public void mapToNewFailureWithDifferentTypeNullMappingFailure() {
        //given
        SimpleFailable<String> source = SimpleFailable.failure("some value");

        //when
        //then
        assertThrows(AssertionError.class, ()->{
            source.mapFailure(a -> null);
        });
    }

    @Test
    @DisplayName("map to a new type - null mapping should not throw an exception on success")
    public void mapToNewFailureWithDifferentTypeNullOutcome() {
        //given
        SimpleFailable<String> source = SimpleFailable.success();

        //when
        SimpleFailable<String> mapped = source.mapFailure(a -> null);

        //then
        assertTrue(mapped.isSuccess());
    }
}