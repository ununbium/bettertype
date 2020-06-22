package dev.errant.bettertype.basic.converter.exception.provided;

import dev.errant.bettertype.basic.converter.exception.ExceptionConverter;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StackTracePrintingExceptionConverter implements ExceptionConverter<String> {

    @Override
    public String convert(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);

        return sw.toString();
    }

}
