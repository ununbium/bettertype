package dev.errant.bettertype.basic.converter.throwable.provided;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StackTracePrintingThrowableConverterTest {

    @Test
    @DisplayName("print stack trace to string")
    public void printStackTraceToString() {
        //given
        StackTracePrintingThrowableConverter converter = new StackTracePrintingThrowableConverter();

        String message = "bang";
        Throwable t = mock(Throwable.class);

        doAnswer( (invocation) -> {
            Object[] args = invocation.getArguments();
            ((PrintWriter) args[0]).print(message);
            return null;
        }).when(t).printStackTrace(any(PrintWriter.class));

        //when
        String output = converter.convert(t);

        //then
        assertEquals(message, output);
     }

}