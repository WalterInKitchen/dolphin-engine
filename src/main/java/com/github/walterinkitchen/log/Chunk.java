package com.github.walterinkitchen.log;

import com.github.walterinkitchen.exception.IntegrityBrokenError;

import java.util.Optional;

/**
 * Data chunk
 *
 * @author walter
 * @since 1.0
 */
public class Chunk implements BinaryDurable {
    private final long adder;
    private final int payloadSize;
    private final byte[] payload;
    private final long crc;

    private static final int HEADER_SIZE = 4;
    private static final int TAIL_SIZE = 8;
    private static final int NON_PAYLOAD_SIZE = HEADER_SIZE + TAIL_SIZE;

    private Chunk(long adder, int payloadSize, byte[] payload, long crc) {
        this.adder = adder;
        this.payloadSize = payloadSize;
        this.payload = payload;
        this.crc = crc;
    }

    @Override
    public long address() {
        return this.adder;
    }

    @Override
    public int payloadSize() {
        return this.payloadSize;
    }

    @Override
    public byte[] payload() {
        return this.payload;
    }

    @Override
    public byte[] toBinary() {
        byte[] res = new byte[binarySize()];
        BinaryUtils.int2Bytes(this.payloadSize, res, 0);
        int offset = 4;
        System.arraycopy(this.payload, 0, res, offset, this.payload.length);
        offset += this.payload.length;
        BinaryUtils.long2Bytes(crc, res, offset);
        return res;
    }

    @Override
    public int binarySize() {
        return this.payloadSize + NON_PAYLOAD_SIZE;
    }

    @Override
    public long crc() {
        return this.crc;
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
        byte[] payload = new byte[payloadSize];
        System.arraycopy(bytes, 4, payload, 0, payloadSize);
        long crc = BinaryUtils.bytes2Long(bytes, 4 + payloadSize);
        long realCrc = BinaryUtils.crc32(payloadSize, payload);
        if (realCrc - crc != 0) {
            throw new IntegrityBrokenError("binary is broken,crc not matched");
        }
        return new Chunk(adder, payloadSize, payload, crc);
    }

    /**
     * build with payload
     *
     * @param payload payload
     * @return instance
     */
    public static Chunk build(byte[] payload) {
        byte[] bytes = Optional.ofNullable(payload).orElse(new byte[0]);
        long crc = BinaryUtils.crc32(bytes.length, bytes);
        return new Chunk(-1, bytes.length, bytes, crc);
    }
}
