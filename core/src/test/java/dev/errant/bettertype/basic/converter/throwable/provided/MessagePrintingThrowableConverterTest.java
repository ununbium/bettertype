package dev.errant.bettertype.basic.converter.throwable.provided;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessagePrintingThrowableConverterTest {

    @Test
    @DisplayName("printMessage")
    public void printMessage() {
        //given
        MessagePrintingThrowableConverter converter = new MessagePrintingThrowableConverter();
        String message = "Bang!";
        Throwable t = new Exception(message);

        //when
        String output = converter.convert(t);

        //then
        assertEquals(message, output);
     }

}