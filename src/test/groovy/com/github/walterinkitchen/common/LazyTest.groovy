package com.github.walterinkitchen.common

import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Supplier

/**
 * Lazy Wrapper test
 * @author walter
 * @since 1.0
 */
class LazyTest extends Specification {
    def 'get from lazy should invoke exactly once'() {
        given: 'supplier'
        AtomicInteger atomicInteger = new AtomicInteger(0)
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            Integer get() {
                return atomicInteger.incrementAndGet()
            }
        }

        when: 'get from lazy'
        Lazy<Integer> lazy = Lazy.of(supplier)

        then: 'should invoke once'
        lazy.get() == 1
        lazy.get() == 1
        lazy.get() == 1
        atomicInteger.get() == 1
    }
}
