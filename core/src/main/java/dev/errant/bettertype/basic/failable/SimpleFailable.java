package dev.errant.bettertype.basic.failable;

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
}
