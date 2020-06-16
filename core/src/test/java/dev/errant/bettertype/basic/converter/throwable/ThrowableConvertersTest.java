package dev.errant.bettertype.basic.converter.throwable;

import dev.errant.bettertype.basic.converter.throwable.provided.MessagePrintingThrowableConverter;
import dev.errant.bettertype.basic.converter.throwable.provided.StackTracePrintingThrowableConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThrowableConvertersTest {

    @Test
    @DisplayName("create messagePrintingConverter")
    public void createMessagePrintingConverter() {
        //given

        //when
        ThrowableConverter<String> converter = ThrowableConverters.messagePrintingConverter();

        //then
        assertEquals(MessagePrintingThrowableConverter.class, converter.getClass());
    }

    @Test
    @DisplayName("create messagePrintingConverter")
    public void createStackTracePrintingConverter() {
        //given

        //when
        ThrowableConverter<String> converter = ThrowableConverters.stackTracePrintingConverter();

        //then
        assertEquals(StackTracePrintingThrowableConverter.class, converter.getClass());
    }

}