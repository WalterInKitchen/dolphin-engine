package com.github.walterinkitchen.log;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.Arrays;
import java.util.Optional;

/**
 * INode
 * storageMap:
 * payloadSize[4],id[8],chunkSize[4],chunks[n*8],refAdder[8],crc[8]
 *
 * @author walter
 * @since 1.0
 */
@Builder(access = AccessLevel.PRIVATE)
public class INode implements BinaryDurable {
    private static final int PAYLOAD_SIZE_OFFSET = 0;
    private static final int ID_OFFSET = PAYLOAD_SIZE_OFFSET + 4;
    private static final int CHUNK_SIZE_OFFSET = ID_OFFSET + 8;
    private static final int CHUNK_ADDRESS_OFFSET = CHUNK_SIZE_OFFSET + 4;
    private final long adder;
    private final byte[] binary;

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
        return Arrays.copyOfRange(this.binary, ID_OFFSET, binarySize() - 8);
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
        int end = binarySize();
        byte[] source = Arrays.copyOfRange(this.binary, end - 8, end);
        return BinaryUtils.bytes2Long(source, 0);
    }

    /**
     * build instance from required properties
     *
     * @param id           id
     * @param chunkAddress chunk address
     * @param refAdder     ref adder
     * @return instance
     */
    public static INode build(long id, long[] chunkAddress, long refAdder) {
        long[] chunks = Optional.ofNullable(chunkAddress).orElse(new long[0]);
        int payloadSize = 8 + 4 + chunks.length * 8 + 8;
        int binarySize = 4 + payloadSize + 8;

        byte[] binary = new byte[binarySize];
        BinaryUtils.int2Bytes(payloadSize, binary, PAYLOAD_SIZE_OFFSET);
        BinaryUtils.long2Bytes(id, binary, ID_OFFSET);
        BinaryUtils.int2Bytes(chunks.length, binary, CHUNK_SIZE_OFFSET);

        int offset = CHUNK_ADDRESS_OFFSET;
        for (long address : chunks) {
            BinaryUtils.long2Bytes(address, binary, offset);
            offset += 8;
        }
        BinaryUtils.long2Bytes(refAdder, binary, offset);
        offset += 8;
        long crc = BinaryUtils.crc32(binary, PAYLOAD_SIZE_OFFSET, offset);
        BinaryUtils.long2Bytes(crc, binary, offset);

        return INode.builder()
                .binary(binary)
                .build();
    }

    public long getId() {
        return BinaryUtils.bytes2Long(this.binary, ID_OFFSET);
    }

    public int getChunkSize() {
        return BinaryUtils.bytes2Int(this.binary, CHUNK_SIZE_OFFSET);
    }

    public long[] getChunkAddress() {
        int chunkSize = this.getChunkSize();
        long[] res = new long[chunkSize];

        int offset = CHUNK_ADDRESS_OFFSET;
        for (int i = 0; i < chunkSize; i++) {
            long adder = BinaryUtils.bytes2Long(this.binary, offset);
            res[i] = adder;
            offset += 8;
        }
        return res;
    }

    public long getRefAdder() {
        int chunkSize = getChunkSize();
        int offset = chunkSize * 8 + CHUNK_ADDRESS_OFFSET;
        return BinaryUtils.bytes2Long(this.binary, offset);
    }
}
