package dev.errant.bettertype.basic.converter.exception.provided;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StackTracePrintingExceptionConverterTest {

    @Test
    @DisplayName("print stack trace to string")
    public void printStackTraceToString() {
        //given
        StackTracePrintingExceptionConverter converter = new StackTracePrintingExceptionConverter();

        String message = "bang";
        Exception e = mock(Exception.class);

        doAnswer( (invocation) -> {
            Object[] args = invocation.getArguments();
            ((PrintWriter) args[0]).print(message);
            return null;
        }).when(e).printStackTrace(any(PrintWriter.class));

        //when
        String output = converter.convert(e);

        //then
        assertEquals(message, output);
     }

}