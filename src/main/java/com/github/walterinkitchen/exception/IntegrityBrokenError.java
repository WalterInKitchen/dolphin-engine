package com.github.walterinkitchen.exception;

/**
 * Integrity was broken error
 *
 * @author walter
 * @since 1.0
 */
public class IntegrityBrokenError extends BaseException {
    /**
     * constructor
     *
     * @param msg msg
     */
    public IntegrityBrokenError(String msg) {
        super(GlobalExceptionCode.INTEGRITY_BROKEN_ERROR, msg);
    }
}
