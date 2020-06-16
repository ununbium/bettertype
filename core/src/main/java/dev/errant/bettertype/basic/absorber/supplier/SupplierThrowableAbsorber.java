package dev.errant.bettertype.basic.absorber.supplier;

import dev.errant.bettertype.basic.converter.throwable.ThrowableConverter;
import dev.errant.bettertype.basic.failable.Failable;

public class SupplierThrowableAbsorber {

    /**
     * runs the action function, returning the result as the "success" value of a failable. If an exception is thrown
     * the exception becomes the failure value.
     *
     * @param action the action to perform
     * @param <S> the success type (inferred from action)
     * @return failable either with the success value or the caught exception
     */
    public static <S> Failable<S, Throwable> absorb(AbsorbableSupplierAction<S> action) {
        return absorb(action, (value) -> value );
    }

    /**
     * Runs the action function, returning the result as the "success" value of a failable. If an exception is thrown
     * the exception is passed to the converter, and the output of the converter becomes the failure value.
     *
     * @param action the action to perform
     * @param converter a converter to transform a throwable into a more useful type
     * @param <S> the success type (inferred from action)
     * @param <F> the failable type (inferred from converter)
     * @return A Failable containing either the Success or Failure value
     */
    public static <S, F> Failable<S,F> absorb(AbsorbableSupplierAction<S> action, ThrowableConverter< F> converter) {
        S outcome = null;
        F failure = null;

        try {
            outcome = action.act();
        } catch (Throwable throwable) {
            failure = converter.convert(throwable);;
        }

        if(outcome!=null) {
            return Failable.success(outcome);
        } else {
            return Failable.failure(failure);
        }
    }

}
