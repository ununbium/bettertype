package dev.errant.bettertype.basic.absorber.action;

import dev.errant.bettertype.basic.converter.throwable.ThrowableConverter;
import dev.errant.bettertype.basic.failable.SimpleFailable;

public class ActionThrowableAbsorber {

    /**
     * Runs the action function, returning the result as the "success" value of a failable. If a throwable is thrown,
     * the it becomes the failure value.
     *
     * @param action the action to perform
     * @return
     */
    public static SimpleFailable<Throwable> absorb(AbsorbableAction action) {
        return absorb(action, (value) -> value );
    }

    /**
     * Runs the action function, returning success if no throwables are thrown. If an throwables is thrown
     * the throwable is passed to the converter, and the output of the converter becomes the failure value.
     *
     * @param action the action to perform
     * @param converter a converter to transform a throwable into a more useful type
     * @param <F> the failable type (inferred from converter)
     * @return A Failable containing either the Success or Failure value
     */
    public static <F> SimpleFailable<F> absorb(AbsorbableAction action, ThrowableConverter<F> converter) {
        F failure = null;

        try {
            action.act();
        } catch (Throwable throwable) {
            failure = converter.convert(throwable);;
        }

        if(failure==null) {
            return SimpleFailable.success();
        } else {
            return SimpleFailable.failure(failure);
        }
    }

}
