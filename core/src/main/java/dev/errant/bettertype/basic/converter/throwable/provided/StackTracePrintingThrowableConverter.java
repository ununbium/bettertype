package dev.errant.bettertype.basic.converter.throwable.provided;

import dev.errant.bettertype.basic.converter.throwable.ThrowableConverter;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StackTracePrintingThrowableConverter implements ThrowableConverter<String> {

    @Override
    public String convert(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);

        return sw.toString();
    }

}
