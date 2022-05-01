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
    @DisplayName("convert a success Failable to a success SimpleFailable")
    public void toSimpleFailable_success() {
        //given
        Failable<Object, String> successFailable = Failable.success("some success value");

        //when
        SimpleFailable<String> result = successFailable.toSimpleFailable();

        //then
        assertTrue(result.isSuccess());
    }

    @Test
    @DisplayName("convert a success Failable to a success SimpleFailable")
    public void toSimpleFailable_failure() {
        //given
        Failable<Object, String> successFailable = Failable.failure("some failure value");

        //when
        SimpleFailable<String> result = successFailable.toSimpleFailable();

        //then
        assertTrue(result.isFailure());
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

    @Test
    @DisplayName("map failure to a new type - failure value is mapped to same type")
    public void mapToNewFailure() {
        //given
        Failable<Integer, String> source = Failable.failure("some message");

        //when
        Failable<Integer, String> mapped = source.mapFailure(a -> a + " additional text");


        //then
        assertTrue(mapped.isFailure());
        assertEquals("some message additional text", mapped.getFailure());
    }


    @Test
    @DisplayName("map failure to a new type - failure value is mapped to different type")
    public void mapToNewFailureWithDifferentType() {
        //given
        Failable<Integer, String> source = Failable.failure("some message");

        //when
        Failable<Integer, Integer> mapped = source.mapFailure(a -> a.length());

        //then
        assertTrue(mapped.isFailure());
        assertEquals(12, mapped.getFailure());
    }

    @Test
    @DisplayName("map failure to a new type - success")
    public void mapToNewFailureWithDifferentTypeSuccess() {
        //given
        Failable<Integer, String> source = Failable.success(3312121);

        //when
        Failable<Integer, Integer> mapped = source.mapFailure(a -> a.length());

        //then
        assertTrue(mapped.isSuccess());
        assertEquals(3312121, mapped.getSuccess());
    }

    @Test
    @DisplayName("map failure to a new type - null mapping should throw an exception on failure")
    public void mapToNewFailureWithDifferentTypeNullMappingFailure() {
        //given
        Failable<Integer, String> source = Failable.failure("some message");

        //when
        //then
        assertThrows(AssertionError.class, ()->{
            source.mapFailure(a -> null);
        });
    }

    @Test
    @DisplayName("map failure to a new type - null mapping should not throw an exception on success")
    public void mapToNewFailureWithDifferentTypeNullOutcome() {
        //given
        Failable<Integer, String> source = Failable.success(623452);

        //when
        Failable<Integer, String> mapped = source.mapFailure(a -> null);

        //then
        assertTrue(mapped.isSuccess());
        assertEquals(623452, mapped.getSuccess());
    }

    @Test
    @DisplayName("map success to a new type - success value is mapped to same type")
    public void mapToNewSuccess() {
        //given
        Failable<Integer, String> source = Failable.success(523432);

        //when
        Failable<Integer, String> mapped = source.mapSuccess(a -> a + 7);

        //then
        assertTrue(mapped.isSuccess());
        assertEquals(523439, mapped.getSuccess());
    }

    @Test
    @DisplayName("map success to a new type - success value is mapped to different type")
    public void mapToNewSuccessWithDifferentType() {
        //given
        Failable<Integer, String> source = Failable.success(523432);

        //when
        Failable<String, String> mapped = source.mapSuccess(a -> a.toString());

        //then
        assertTrue(mapped.isSuccess());
        assertEquals("523432", mapped.getSuccess());
    }

    @Test
    @DisplayName("map success to a new type - failure")
    public void mapToNewSuccessWithDifferentTypeSuccess() {
        //given
        Failable<Integer, String> source = Failable.failure("some value here");

        //when
        Failable<String, String> mapped = source.mapSuccess(a -> a + "");

        //then
        assertTrue(mapped.isFailure());
        assertEquals("some value here", mapped.getFailure());
    }

    @Test
    @DisplayName("map success to a new type - null mapping should throw an exception on failure")
    public void mapToNewSuccessWithDifferentTypeNullMappingFailure() {
        //given
        Failable<Integer, String> source = Failable.success(6233454);

        //when
        //then
        assertThrows(AssertionError.class, ()->{
            source.mapSuccess(a -> null);
        });
    }

    @Test
    @DisplayName("map success to a new type - null mapping should not throw an exception on success")
    public void mapToNewSuccessWithDifferentTypeNullOutcome() {
        //given
        Failable<Integer, String> source = Failable.success(623452);

        //when
        Failable<Integer, String> mapped = source.mapFailure(a -> null);

        //then
        assertTrue(mapped.isSuccess());
        assertEquals(623452, mapped.getSuccess());
    }
}
