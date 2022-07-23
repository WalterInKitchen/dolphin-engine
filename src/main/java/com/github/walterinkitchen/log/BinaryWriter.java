package com.github.walterinkitchen.log;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Binary writer
 *
 * @author walter
 * @since 1.0
 */
public interface BinaryWriter {
    /**
     * write bytes at start
     *
     * @param start  the start address
     * @param buffer the bytes buffer
     */
    void write(long start, ByteBuffer buffer) throws IOException;

    /**
     * write bytes at start
     *
     * @param start  the start address
     * @param bytes the bytes buffer
     */
    void write(long start, byte[] bytes) throws IOException;
}
