package com.github.walterinkitchen.exception;

import lombok.Getter;

/**
 * Base exception
 *
 * @author walter
 * @since 1.0
 */
@Getter
public class BaseException extends RuntimeException {
    private final String code;
    private final String msg;
    private final String extra;

    /**
     * build an exception instance
     *
     * @param code  code
     * @param msg   msg
     * @param extra extra
     */
    public BaseException(String code, String msg, String extra) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.extra = extra;
    }

    /**
     * build an exception instance
     *
     * @param code code
     * @param msg  msg
     */
    public BaseException(String code, String msg) {
        this(code, msg, "");
    }

    /**
     * build an exception instance
     *
     * @param code code
     */
    public BaseException(String code) {
        this(code, "", "");
    }
}
