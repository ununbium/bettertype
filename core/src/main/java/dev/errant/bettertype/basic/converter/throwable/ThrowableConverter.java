package dev.errant.bettertype.basic.converter.throwable;

/**
 * Convert a throwable to a more useful type.
 *
 * Be careful when implementing this - different types of exception can be thrown and your code should account for this.
 *
 * @param <O> the target type of the converter
 */
@FunctionalInterface
public interface ThrowableConverter<O> {
    /**
     * Convert the throwable into a representative type
     *
     * @param throwable the throwable to convert
     * @return the converted throwable
     */
    O convert(Throwable throwable);
}
