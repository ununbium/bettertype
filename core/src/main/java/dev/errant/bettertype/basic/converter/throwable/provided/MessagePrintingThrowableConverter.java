package dev.errant.bettertype.basic.converter.throwable.provided;

import dev.errant.bettertype.basic.converter.throwable.ThrowableConverter;

public class MessagePrintingThrowableConverter implements ThrowableConverter<String> {

    @Override
    public String convert(Throwable throwable) {
        return throwable.getMessage();
    }

}
