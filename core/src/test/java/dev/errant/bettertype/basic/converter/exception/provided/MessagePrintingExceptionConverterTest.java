package dev.errant.bettertype.basic.converter.exception.provided;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessagePrintingExceptionConverterTest {

    @Test
    @DisplayName("printMessage")
    public void printMessage() {
        //given
        MessagePrintingExceptionConverter converter = new MessagePrintingExceptionConverter();
        String message = "Bang!";
        Exception e = new Exception(message);

        //when
        String output = converter.convert(e);

        //then
        assertEquals(message, output);
     }

}