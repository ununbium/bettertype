package dev.errant.bettertype.basic.converter.exception.provided;

import dev.errant.bettertype.basic.converter.exception.ExceptionConverter;

public class MessagePrintingExceptionConverter implements ExceptionConverter<String> {

    @Override
    public String convert(Exception exception) {
        return exception.getMessage();
    }

}
