package dev.errant.bettertype.basic.failable;

import dev.errant.bettertype.basic.absorber.NullValueAbsorbed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        Failable<Object, Exception> absorb = Failable.absorb(() -> {
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
        Failable<String, Exception> absorb = Failable.absorb(() -> successValue);

        //then
        assertTrue(absorb.isSuccess());
        assertEquals(successValue, absorb.getSuccess());
    }

    @Test
    @DisplayName("absorb the exception of a supplier action and convert it")
    public void absorbExceptionConverted() {
        //given
        Exception e = mock(Exception.class);
        String message = "bang";
        when(e.getMessage()).thenReturn(message);

        //when
        Failable<Integer, String> absorb = Failable.absorb(() -> {
            throw e;
        }, Exception::getMessage);

        //then
        assertTrue(absorb.isFailure());
        assertEquals(message, absorb.getFailure());
    }

    @Test
    @DisplayName("absorb the exception of supplier action and convert it")
    public void absorbNull() {
        //given

        //when
        Failable<Integer, Exception> absorb = Failable.absorb(
                () -> null,
                (t) -> t);

        //then
        assertTrue(absorb.isFailure());
        assertEquals(NullValueAbsorbed.class, absorb.getFailure().getClass());
    }

    @Test
    @DisplayName("convert a failable to a 'present' Optional")
    public void toOptional_success() {
        //given
        String someValue = "some value";
        Failable<String, Object> success = Failable.success(someValue);

        //when
        Optional<String> optionalResult = success.toOptional();

        //then
        assertTrue(optionalResult.isPresent());
        assertEquals(optionalResult.get(), someValue);
    }

    @Test
    @DisplayName("convert a failable to a 'empty' Optional")
    public void toOptional_failure() {
        //given
        String someFailure = "some failure";
        Failable<String, Object> failure = Failable.failure(someFailure);

        //when
        Optional<String> optionalResult = failure.toOptional();

        //then
        assertTrue(optionalResult.isEmpty());
    }

    @Test
    @DisplayName("convert a failable to a 'present' Optional with an (uncalled) failure handler")
    public void toOptional_successWithHandler() {
        //given
        String someValue = "some value";
        Failable<String, Integer> success = Failable.success(someValue);

        Consumer<Integer> mockFailureHandler = mock(Consumer.class);

        //when
        Optional<String> optionalResult = success.toOptional(mockFailureHandler);

        //then
        assertTrue(optionalResult.isPresent());
        assertEquals(optionalResult.get(), someValue);

        verifyZeroInteractions(mockFailureHandler);
    }

    @Test
    @DisplayName("convert a failable to a 'present' Optional with a (called) failure handler")
    public void toOptional_failureWithHandler() {
        //given
        Integer someFailure = 7345;
        Failable<String, Integer> failure = Failable.failure(someFailure);

        Consumer<Integer> mockFailureHandler = mock(Consumer.class);

        //when
        Optional<String> optionalResult = failure.toOptional(mockFailureHandler);

        //then
        assertTrue(optionalResult.isEmpty());

        verify(mockFailureHandler).accept(someFailure);
    }
}
