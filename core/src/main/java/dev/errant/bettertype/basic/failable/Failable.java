package dev.errant.bettertype.basic.failable;

import dev.errant.bettertype.basic.absorber.AbsorbableSupplierAction;
import dev.errant.bettertype.basic.absorber.NullValueAbsorbed;
import dev.errant.bettertype.basic.converter.exception.ExceptionConverter;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

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
     * Convert this Failable to an Optional of the Success type.
     *
     * @return an optional representing the success case (optionally present if success)
     */
    public Optional<S> toOptional() {
        return Optional.ofNullable(successValue);
    }

    /**
     * Convert this Failable to an Optional of the SimpleFailable type.
     *
     * @return a SimpleFailable representing the failure case (discarding the success value)
     */
    public SimpleFailable<F> toSimpleFailable() {
        final SimpleFailable<F> result;

        if(isFailure()) {
            result = SimpleFailable.failure(failValue);
        } else {
            result = SimpleFailable.success();
        }

        return result;
    }

    /**
     * Convert this Failable to an Optional of the Success type. If this Failable is a failure, the failureHandler is
     * first called to handle the failure.
     *
     * @param failureHandler handles the failure value
     * @return an optional representing the success case (optionally present if success)
     */
    public Optional<S> toOptional(Consumer<F> failureHandler) {
        if(isFailure()) {
            failureHandler.accept(failValue);
        }

        return Optional.ofNullable(successValue);
    }

    /**
     * Convert this Failable to a new Failable, mapping the success value if present.
     *
     * @param converter converts the current success value to a new value
     * @param <nS> the type of the new success value
     * @return an update Failable with a mapped success value if present
     */
    public <nS> Failable<nS, F> mapSuccess(Function<S, nS> converter) {
        final Failable<nS, F> result;

        if(isSuccess()) {
            nS convertedSuccess = converter.apply(getSuccess());
            result = Failable.success(convertedSuccess);
        } else {
            result = Failable.failure(getFailure());
        }

        return result;
    }

    /**
     * Convert this Failable to a new Failable, mapping the failure value if present.
     *
     * @param converter converts the current failure value to a new value
     * @param <nF> the type of the new failure value
     * @return an update Failable with a mapped failure value, if present
     */
    public <nF> Failable<S, nF> mapFailure(Function<F, nF> converter) {
        final Failable<S, nF> result;

        if(isFailure()) {
            nF convertedFailure = converter.apply(getFailure());
            result = Failable.failure(convertedFailure);
        } else {
            result = Failable.success(getSuccess());
        }

        return result;
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
     * If the AbsorbableSupplierAction returns a null value, this is treated as a "Failure" with a marker exception of
     * "NullValueAbsorbed" being given as the failure value.
     *
     * @param action the action to perform
     * @param <S> the success type (inferred from action)
     * @return failable either with the success value or the caught exception
     */
    public static <S> Failable<S, Exception> absorb(AbsorbableSupplierAction<S> action) {
        return absorb(action, (value) -> value );
    }

    /**
     * Runs the action function, returning the result as the "success" value of a failable. If an exception is thrown
     * the exception is passed to the converter, and the output of the converter becomes the failure value.
     *
     * If the AbsorbableSupplierAction returns a null value, this is treated as a "Failure" with a marker exception of
     * "NullValueAbsorbed" being passed to the ExceptionConverter for appropriate conversion to a Failure value.
     *
     * @param action the action to perform
     * @param converter a converter to transform a Exception into a more useful type
     * @param <S> the success type (inferred from action)
     * @param <F> the failable type (inferred from converter)
     * @return A Failable containing either the Success or Failure value
     */
    public static <S, F> Failable<S,F> absorb(AbsorbableSupplierAction<S> action, ExceptionConverter< F> converter) {
        S outcome = null;
        F failure = null;

        try {
            outcome = action.act();
        } catch (Exception exception) {
            failure = converter.convert(exception);
        }

        if(outcome!=null) {
            return Failable.success(outcome);
        } else {
            if(failure==null) {
                failure = converter.convert(new NullValueAbsorbed());
            }
            return Failable.failure(failure);
        }
    }

}
