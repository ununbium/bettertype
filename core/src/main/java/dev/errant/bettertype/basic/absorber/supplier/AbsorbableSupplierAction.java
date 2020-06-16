package dev.errant.bettertype.basic.absorber.supplier;

@FunctionalInterface
public interface AbsorbableSupplierAction<T> {
    T act() throws Throwable;
}
