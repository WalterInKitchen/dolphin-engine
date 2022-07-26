package com.github.walterinkitchen.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Global exception code definitions
 *
 * @author walter
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GlobalExceptionCode {
    /**
     * Integrity was broken
     */
    public static final String INTEGRITY_BROKEN_ERROR = "100300";
}
