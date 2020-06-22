package dev.errant.bettertype.basic.failable;

import dev.errant.bettertype.basic.absorber.AbsorbableSupplierAction;
import dev.errant.bettertype.basic.converter.throwable.ThrowableConverter;

import java.util.NoSuchElementException;

/**
 * A Container type used to contain either the successful value, or return a different type indicating an error.
 *
 * Failable has the same basic intent as the built in Optional type, but with a stronger notion of success and failure
 *
 * The failure type F may be an Exception, but in some cases it may make sense for this to be another arbitrary object.
 * For example failing with an enumerated HTTP status, String or complex type.
 */
final public class Failable<S, F> {
    private final S successValue;
    private final F failValue;

    private Failable(S successValue, F failValue) {
        assert(successValue==null || failValue==null);
        assert(! (successValue==null && failValue==null));

        this.successValue = successValue;
        this.failValue = failValue;
    }

    /**
     * @return true if the outcome is successful, false otherwise
     */
    public boolean isSuccess() {
        return successValue != null;
    }

    /**
     * @return true if the outcome is failure, false otherwise
     */
    public boolean isFailure() {
        return failValue != null;
    }

    /**
     * @return if successful, the value of the success
     */
    public S getSuccess() {
        if(isFailure()) {
            throw new NoSuchElementException("This Failable did not succeed");
        }

        return this.successValue;
    }

    /**
     * @return if failure, the value of the failure
     */
    public F getFailure() {
        if(isSuccess()) {
            throw new NoSuchElementException("This Failable value did not fail");
        }

        return this.failValue;
    }

    /**
     * @param successValue the non-null success value
     * @param <S> inferred success type
     * @param <F> inferred failure type
     * @return A Failable representing a success with a value
     */
    public static <S, F> Failable<S, F> success(S successValue) {
        return new Failable<S, F>(successValue, null);
    }

    /**
     * @param failValue the non-null fail value
     * @param <S> inferred success type
     * @param <F> inferred failure type
     * @return A Failable representing a failure with a value
     */
    public static <S, F> Failable<S, F> failure(F failValue) {
        return new Failable<S, F>(null, failValue);
    }

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
            failure = converter.convert(throwable);
        }

        if(outcome!=null) {
            return Failable.success(outcome);
        } else {
            return Failable.failure(failure);
        }
    }

}
