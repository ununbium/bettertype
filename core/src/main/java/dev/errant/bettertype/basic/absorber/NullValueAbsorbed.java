package dev.errant.bettertype.basic.absorber;

/**
 * An Exception that represents a particular situation; the AbsorbableSupplierAction returned null when trying to absorb
 * errors from a method. The absorber is strongly opinionated about this situation - it is a "failure" case and as such
 * it is up to the ExceptionConverter to handle the failure value generation.
 *
 * NullValueAbsorbed is never thrown. It is only ever passed to the ExceptionConverter in the event that the
 * AbsorbableSupplierAction output was null.
 *
 * This is a compromise between keeping a simple API for implementers and handling nulls. This approach retains the
 * ability to define the ExceptionConverter inline as a function.
 */
public class NullValueAbsorbed extends Exception {

    public NullValueAbsorbed() {
        super("a null return value was absorbed");
    }

}
