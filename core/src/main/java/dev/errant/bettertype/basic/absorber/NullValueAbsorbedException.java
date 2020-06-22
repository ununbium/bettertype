package dev.errant.bettertype.basic.absorber;

public class NullValueAbsorbedException extends Exception {

    public NullValueAbsorbedException() {
        super("a null return value was absorbed");
    }

}
