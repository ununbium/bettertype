package dev.errant.bettertype.basic.absorber;

@FunctionalInterface
public interface AbsorbableAction {
    void act() throws Throwable;
}
