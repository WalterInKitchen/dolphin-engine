package com.github.walterinkitchen.log;

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
        return this.payloadSize + 8 + 4;
    }

    @Override
    public long crc() {
        return this.crc;
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
