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
        res[0] = (byte) ((src >> 56) & 0xff);
        res[1] = (byte) ((src >> 48) & 0xff);
        res[2] = (byte) ((src >> 40) & 0xff);
        res[3] = (byte) ((src >> 32) & 0xff);
        res[4] = (byte) ((src >> 24) & 0xff);
        res[5] = (byte) ((src >> 16) & 0xff);
        res[6] = (byte) ((src >> 8) & 0xff);
        res[7] = (byte) (src & 0xff);
        return res;
    }

    /**
     * int to bytes
     *
     * @param src src
     * @return bytes nvever null
     */
    public static byte[] int2Bytes(int src) {
        byte[] res = new byte[4];
        res[0] = (byte) ((src >> 24) & 0xff);
        res[1] = (byte) ((src >> 16) & 0xff);
        res[2] = (byte) ((src >> 8) & 0xff);
        res[3] = (byte) (src & 0xff);
        return res;
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
        CRC32C crc32C = new CRC32C();
        crc32C.update(bytes, 0, bytes.length);
        return crc32C.getValue();
    }
}
