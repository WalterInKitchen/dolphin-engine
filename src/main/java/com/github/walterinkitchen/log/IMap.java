package com.github.walterinkitchen.log;

import lombok.AccessLevel;
import lombok.Builder;

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
        return 4;
    }

    @Override
    public byte[] payload() {
        return new byte[0];
    }

    @Override
    public byte[] toBinary() {
        return new byte[0];
    }

    @Override
    public int binarySize() {
        return 16;
    }

    @Override
    public long crc() {
        return 0;
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
        Optional<Map.Entry<Long, Long>> invalid = nodes.entrySet().stream()
                .filter(ety -> ety.getKey() == null || ety.getValue() == null)
                .findFirst();
        if (invalid.isPresent()) {
            throw new IllegalArgumentException();
        }

        return IMap.builder().build();
    }
}
