package dev.errant.bettertype.basic.converter.exception;


import dev.errant.bettertype.basic.converter.exception.provided.MessagePrintingExceptionConverter;
import dev.errant.bettertype.basic.converter.exception.provided.StackTracePrintingExceptionConverter;

/**
 * A convenience class that constructs various types of converters
 */
public class ExceptionConverters {

    /**
     * @return a ExceptionConverter that produces a classic Java stack trace to a string with messages and chained causes also printed
     */
    public static ExceptionConverter<String> stackTracePrintingConverter() {
        return new StackTracePrintingExceptionConverter();
    }

    /**
     * @return a ExceptionConverter that produces just the message from the Exception (no stack traces or nested messages are printed)
     */
    public static ExceptionConverter<String> messagePrintingConverter() {
        return new MessagePrintingExceptionConverter();
    }


}
