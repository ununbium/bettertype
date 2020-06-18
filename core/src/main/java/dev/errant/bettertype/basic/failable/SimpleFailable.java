package dev.errant.bettertype.basic.failable;

import dev.errant.bettertype.basic.absorber.AbsorbableAction;
import dev.errant.bettertype.basic.converter.throwable.ThrowableConverter;

import java.util.NoSuchElementException;

/**
 * A Container type used to communicate either a success or an error value.
 *
 * Failable has the same basic intent as the built in Optional type, but with Failure semantics
 *
 * The failure type F may be an Exception, but in some cases it may make sense for this to be another arbitrary object.
 * For example failing with an enumerated HTTP status, String or complex type.
 */
final public class SimpleFailable<F> {
    private final F failValue;

    private SimpleFailable(F failValue) {
        this.failValue = failValue;
    }

    /**
     * @return true if the outcome is successful, false otherwise
     */
    public boolean isSuccess() {
        return failValue == null;
    }

    /**
     * @return true if the outcome is failure, false otherwise
     */
    public boolean isFailure() {
        return !isSuccess();
    }

    /**
     * @return if failed, the value of the failure
     * @throws NoSuchElementException if the instance is successful (check before calling)
     */
    public F getFailure() {
        if(isSuccess()) {
            throw new NoSuchElementException("This Failable did not fail");
        }

        return this.failValue;
    }

    /**
     * @param <F> the inferred type of the failure
     * @return A SimpleFailable representing a success with no value
     */
    public static <F> SimpleFailable<F> success() {
        return new SimpleFailable<F>(null);
    }

    /**
     * @param <F> the inferred type of the failure
     * @return A SimpleFailable representing a failure with a value
     */
    public static <F> SimpleFailable<F> failure(F failValue) {
        assert(failValue!=null);
        return new SimpleFailable<F>(failValue);
    }

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
