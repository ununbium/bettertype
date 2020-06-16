package dev.errant.bettertype.basic.absorber.action;

@FunctionalInterface
public interface AbsorbableAction {
    void act() throws Throwable;
}
