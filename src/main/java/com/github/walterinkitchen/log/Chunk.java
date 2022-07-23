package com.github.walterinkitchen.log;

/**
 * Data chunk
 *
 * @author walter
 * @since 1.0
 */
public class Chunk {
    private byte[] bytes;
    private long crc;

    public long getSize() {
        if (bytes == null) {
            return 0;
        }
        return this.bytes.length;
    }
}
