package com.github.walterinkitchen.log;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * iMap
 * payloadSize[4]+iNodeSize[4]+iNodesId[n*8]+iNodesAdder[n*8]+crc[8]
 *
 * @author walter
 * @since 1.0
 */
@Builder(access = AccessLevel.PRIVATE)
public class IMap implements BinaryDurable {
    private static final int PAYLOAD_OFFSET = 0;
    private static final int INODE_SIZE_OFFSET = 4;
    private static final int INODE_ID_OFFSET = INODE_SIZE_OFFSET + 4;

    private long adder;
    private byte[] binary;

    @Override
    public long address() {
        return this.adder;
    }

    @Override
    public int payloadSize() {
        return BinaryUtils.bytes2Int(this.binary, PAYLOAD_OFFSET);
    }

    @Override
    public byte[] payload() {
        return Arrays.copyOfRange(this.binary, INODE_SIZE_OFFSET, binarySize() - 8);
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
        return BinaryUtils.bytes2Long(this.binary, binarySize() - 8);
    }

    /**
     * build instance
     *
     * @param adder address
     * @param nodes nodes map;key is nodeId,value is node address
     * @return instance
     */
    public static IMap build(long adder, Map<Long, Long> nodes) {
        if (adder < 0 || nodes == null) {
            throw new IllegalArgumentException();
        }
        Optional<Map.Entry<Long, Long>> invalid = nodes
                .entrySet()
                .stream()
                .filter(ety -> ety.getKey() == null || ety.getValue() == null)
                .findFirst();
        if (invalid.isPresent()) {
            throw new IllegalArgumentException();
        }
        int nodesSize = nodes.size();
        int payloadSize = 4 + nodesSize * 8 * 2;
        int binarySize = payloadSize + 4 + 8;
        byte[] binary = new byte[binarySize];
        BinaryUtils.int2Bytes(payloadSize, binary, PAYLOAD_OFFSET);
        byte[] ids = new byte[nodesSize * 8];
        byte[] address = new byte[nodesSize * 8];
        BinaryUtils.int2Bytes(nodesSize, binary, INODE_SIZE_OFFSET);

        int offset = 0;
        for (Map.Entry<Long, Long> entry : nodes.entrySet()) {
            BinaryUtils.long2Bytes(entry.getKey(), ids, offset);
            BinaryUtils.long2Bytes(entry.getValue(), address, offset);
            offset += 8;
        }
        int start = INODE_ID_OFFSET;
        System.arraycopy(ids, 0, binary, start, ids.length);
        start += ids.length;
        System.arraycopy(address, 0, binary, start, address.length);
        start += address.length;

        long crc = BinaryUtils.crc32(binary, 0, start);
        BinaryUtils.long2Bytes(crc, binary, start);
        return IMap.builder().adder(adder).binary(binary).build();
    }
}
