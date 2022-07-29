package com.github.walterinkitchen.log;

import com.github.walterinkitchen.exception.IntegrityBrokenError;

import java.util.Arrays;
import java.util.Optional;

/**
 * Data chunk
 *
 * @author walter
 * @since 1.0
 */
public class Chunk implements BinaryDurable {
    private static final int PAYLOAD_SIZE_OFFSET = 0;
    private static final int PAYLOAD_OFFSET = PAYLOAD_SIZE_OFFSET + 4;
    private static final int HEADER_SIZE = 4;
    private static final int TAIL_SIZE = 8;
    private static final int NON_PAYLOAD_SIZE = HEADER_SIZE + TAIL_SIZE;
    private final long adder;
    private final byte[] binary;

    private Chunk(long adder, byte[] binary) {
        this.adder = adder;
        this.binary = binary;
    }

    @Override
    public long address() {
        return this.adder;
    }

    @Override
    public int payloadSize() {
        return BinaryUtils.bytes2Int(this.binary, PAYLOAD_SIZE_OFFSET);
    }

    @Override
    public byte[] payload() {
        return Arrays.copyOfRange(this.binary, PAYLOAD_OFFSET, binarySize() - 8);
    }

    @Override
    public byte[] toBinary() {
        return this.binary;
    }

    @Override
    public int binarySize() {
        return this.binary.length;
    }

    @Override
    public long crc() {
        int end = this.binary.length;
        return BinaryUtils.bytes2Long(this.binary, end - 8);
    }

    /**
     * build instance from binary bytes
     *
     * @param adder address
     * @param bytes the binary bytes
     * @return instance or {@link IntegrityBrokenError}
     */
    public static Chunk fromBinary(long adder, byte[] bytes) {
        if (bytes == null || adder < 0) {
            throw new IllegalArgumentException();
        }
        if (bytes.length < NON_PAYLOAD_SIZE) {
            throw new IntegrityBrokenError("binary is broken");
        }
        int payloadSize = BinaryUtils.bytes2Int(bytes, 0);
        if (payloadSize + NON_PAYLOAD_SIZE > bytes.length) {
            throw new IntegrityBrokenError("binary is broken,binary not long enough");
        }
        long crc = BinaryUtils.bytes2Long(bytes, 4 + payloadSize);
        long realCrc = BinaryUtils.crc32(bytes, 0, bytes.length - 8);
        if (realCrc - crc != 0) {
            throw new IntegrityBrokenError("binary is broken,crc not matched");
        }
        return new Chunk(adder, bytes);
    }

    /**
     * build with payload
     *
     * @param adder   adder
     * @param payload payload
     * @return instance
     */
    public static Chunk build(long adder, byte[] payload) {
        byte[] bytes = Optional.ofNullable(payload).orElse(new byte[0]);

        int size = bytes.length + 4 + 8;
        byte[] binary = new byte[size];
        BinaryUtils.int2Bytes(bytes.length, binary, 0);
        System.arraycopy(bytes, 0, binary, PAYLOAD_OFFSET, bytes.length);
        long crc = BinaryUtils.crc32(binary, 0, size - 8);
        BinaryUtils.long2Bytes(crc, binary, size - 8);
        return new Chunk(adder, binary);
    }
}
