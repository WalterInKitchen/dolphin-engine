package com.github.walterinkitchen.log;

/**
 * BinaryDurable
 *
 * @author walter
 * @since 1.0
 */
public interface BinaryDurable {
    /**
     * the address
     *
     * @return address
     */
    long address();

    /**
     * payload size
     *
     * @return payload size
     */
    int payloadSize();

    /**
     * get payload bytes
     *
     * @return bytes
     */
    byte[] payload();

    /**
     * convert to binary
     *
     * @return binary bytes
     */
    byte[] toBinary();

    /**
     * return the binary size
     *
     * @return size
     */
    int size();

    /**
     * crc
     *
     * @return crc
     */
    long crc();
}
