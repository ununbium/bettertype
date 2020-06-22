package dev.errant.bettertype.basic.converter.exception;

import dev.errant.bettertype.basic.converter.exception.provided.MessagePrintingExceptionConverter;
import dev.errant.bettertype.basic.converter.exception.provided.StackTracePrintingExceptionConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionConvertersTest {

    @Test
    @DisplayName("create messagePrintingConverter")
    public void createMessagePrintingConverter() {
        //given

        //when
        ExceptionConverter<String> converter = ExceptionConverters.messagePrintingConverter();

        //then
        assertEquals(MessagePrintingExceptionConverter.class, converter.getClass());
    }

    @Test
    @DisplayName("create messagePrintingConverter")
    public void createStackTracePrintingConverter() {
        //given

        //when
        ExceptionConverter<String> converter = ExceptionConverters.stackTracePrintingConverter();

        //then
        assertEquals(StackTracePrintingExceptionConverter.class, converter.getClass());
    }

}