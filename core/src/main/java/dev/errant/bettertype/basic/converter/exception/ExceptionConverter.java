package dev.errant.bettertype.basic.converter.exception;

/**
 * Convert an Exception to a more useful type.
 *
 * Be careful when implementing this - different types of exception can be thrown and your code should account for this.
 *
 * In particular be aware that the {@link dev.errant.bettertype.basic.absorber.NullValueAbsorbed} can be passed as the
 * exception value as a special case - representing that the
 * {@link dev.errant.bettertype.basic.absorber.AbsorbableSupplierAction} returned null.
 *
 * @param <O> the target type of the converter
 */
@FunctionalInterface
public interface ExceptionConverter<O> {
    /**
     * Convert the exception into a representative type
     *
     * @param exception the exception to convert
     * @return the converted exception
     */
    O convert(Exception exception);
}
