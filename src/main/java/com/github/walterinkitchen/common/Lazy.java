package com.github.walterinkitchen.common;

import java.util.function.Supplier;

/**
 * Lazy wrapper
 *
 * @author walter
 * @since 1.0
 */
public class Lazy<T> implements Supplier<T> {
    private final Supplier<T> supplier;
    private volatile T obj;

    private Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        if (this.obj == null) {
            synchronized (this) {
                if (this.obj == null) {
                    this.obj = supplier.get();
                }
            }
        }
        return this.obj;
    }

    /**
     * build an lazy instance
     *
     * @param supplier supplier
     * @param <R>      supplier instance type
     * @return instance
     */
    public static <R> Lazy<R> of(Supplier<R> supplier) {
        return new Lazy<>(supplier);
    }
}
