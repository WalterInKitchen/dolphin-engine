package com.github.walterinkitchen.log;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Binary reader
 *
 * @author walter
 * @since 1.0
 */
public interface BinaryReader {
    /**
     * read size of bytes from start
     *
     * @param start the cursor
     * @param size  size
     * @return bytes buffer
     */
    ByteBuffer read(long start, int size) throws IOException;
}
