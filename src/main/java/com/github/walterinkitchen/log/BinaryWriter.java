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
     * @throws IOException exception if error
     */
    void write(long start, ByteBuffer buffer) throws IOException;

    /**
     * write bytes at start
     *
     * @param start the start address
     * @param bytes the bytes buffer
     * @throws IOException exception if error
     */
    void write(long start, byte[] bytes) throws IOException;

    /**
     * write bytes at end
     *
     * @param bytes bytes
     * @throws IOException exception if error
     */
    void write(byte[] bytes) throws IOException;

    /**
     * write buffer at end
     *
     * @param buffer buffer
     * @throws IOException exception if error
     */
    void write(ByteBuffer buffer) throws IOException;
}
