package dev.errant.bettertype.basic.converter.throwable;


import dev.errant.bettertype.basic.converter.throwable.provided.MessagePrintingThrowableConverter;
import dev.errant.bettertype.basic.converter.throwable.provided.StackTracePrintingThrowableConverter;

/**
 * A convenience class that constructs various types of converters
 */
public class ThrowableConverters {

    /**
     * @return a ThrowableConverter that produces a classic Java stack trace to a string with messages and chained causes also printed
     */
    public static ThrowableConverter<String> stackTracePrintingConverter() {
        return new StackTracePrintingThrowableConverter();
    }

    /**
     * @return a ThrowableConverter that produces just the message from the throwable (no stack traces or nested messages are printed)
     */
    public static ThrowableConverter<String> messagePrintingConverter() {
        return new MessagePrintingThrowableConverter();
    }


}
