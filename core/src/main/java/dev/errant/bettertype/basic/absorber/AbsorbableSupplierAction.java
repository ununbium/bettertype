package dev.errant.bettertype.basic.absorber;

@FunctionalInterface
public interface AbsorbableSupplierAction<T> {
    T act() throws Exception;
}
