package com.github.walterinkitchen.log;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.zip.CRC32C;

/**
 * binary utils
 *
 * @author walter
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BinaryUtils {
    /**
     * convert long to bytes
     *
     * @param src source
     * @return bytes never null
     */
    public static byte[] long2Bytes(long src) {
        byte[] res = new byte[8];
        return long2Bytes(src, res, 0);
    }

    /**
     * convert long to bytes and set to the target array at offset
     * if target array's length not long enough,indexOutOfBoundException will be thrown
     *
     * @param src    long val
     * @param target target array
     * @param offset offset
     * @return the target array with bytes filled at offset
     */
    public static byte[] long2Bytes(long src, byte[] target, int offset) {
        if (offset + 8 > target.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        target[offset] = (byte) ((src >> 56) & 0xff);
        target[1 + offset] = (byte) ((src >> 48) & 0xff);
        target[2 + offset] = (byte) ((src >> 40) & 0xff);
        target[3 + offset] = (byte) ((src >> 32) & 0xff);
        target[4 + offset] = (byte) ((src >> 24) & 0xff);
        target[5 + offset] = (byte) ((src >> 16) & 0xff);
        target[6 + offset] = (byte) ((src >> 8) & 0xff);
        target[7 + offset] = (byte) (src & 0xff);
        return target;
    }

    /**
     * int to bytes
     *
     * @param src src
     * @return bytes nvever null
     */
    public static byte[] int2Bytes(int src) {
        byte[] res = new byte[4];
        return int2Bytes(src, res, 0);
    }

    /**
     * convert int to bytes and set to the target array at offset
     * if target array's length not long enough,indexOutOfBoundException will be thrown
     *
     * @param src    int val
     * @param target target array
     * @param offset offset
     * @return the target array with bytes filled at offset
     */
    public static byte[] int2Bytes(int src, byte[] target, int offset) {
        if (offset + 4 > target.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        target[offset] = (byte) ((src >> 24) & 0xff);
        target[1 + offset] = (byte) ((src >> 16) & 0xff);
        target[2 + offset] = (byte) ((src >> 8) & 0xff);
        target[3 + offset] = (byte) (src & 0xff);
        return target;
    }

    /**
     * bytes to long
     *
     * @param bytes  bytes
     * @param offset offset
     * @param size   byte size
     * @return long
     */
    public static long bytes2Long(byte[] bytes, int offset, int size) {
        return 0l;
    }

    /**
     * bytes to int
     *
     * @param bytes  bytes
     * @param offset offset
     * @param size   byte size
     * @return int
     */
    public static int bytes2Int(byte[] bytes, int offset, int size) {
        return 0;
    }

    /**
     * calculate the crc32 from bytes
     *
     * @param bytes bytes
     * @return crc
     */
    public static long crc32(byte[] bytes) {
        return crc32(bytes, 0, bytes.length);
    }

    /**
     * calculate the crc32 from int and bytes
     *
     * @param intV  int val
     * @param bytes bytes
     * @return crc
     */
    public static long crc32(int intV, byte[] bytes) {
        CRC32C crc32C = new CRC32C();
        crc32C.update(intV);
        crc32C.update(bytes, 0, bytes.length);
        return crc32C.getValue();
    }

    /**
     * calculate the crc32 from bytes
     *
     * @param bytes  bytes
     * @param offset offset
     * @param length length
     * @return crc
     */
    public static long crc32(byte[] bytes, int offset, int length) {
        CRC32C crc32C = new CRC32C();
        crc32C.update(bytes, offset, length);
        return crc32C.getValue();
    }
}
