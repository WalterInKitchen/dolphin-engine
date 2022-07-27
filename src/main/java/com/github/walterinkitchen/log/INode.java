package com.github.walterinkitchen.log;

import com.github.walterinkitchen.common.Lazy;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

/**
 * INode
 *
 * @author walter
 * @since 1.0
 */
@Builder(access = AccessLevel.PRIVATE)
public class INode {
    @Getter
    private final long adder;
    @Getter
    private final int payloadSize;
    @Getter
    private final long id;
    @Getter
    private final byte chunkSize;
    @Getter
    private final long[] chunkAddress;
    @Getter
    private final long refAdder;
    @Getter
    private final long crc;

    private List<Lazy<Chunk>> lazyChunkList;
    private Lazy<INode> lazyRef;

    /**
     * get reference
     *
     * @return instance or null
     */
    public Optional<INode> getRef() {
        if (this.lazyRef == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.lazyRef.get());
    }

    /**
     * get chunk1
     *
     * @return instance
     */
    public Optional<Chunk> getChunkAt(int adder) {
        return Optional.empty();
    }
}
