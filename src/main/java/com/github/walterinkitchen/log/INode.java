package com.github.walterinkitchen.log;

import lombok.Builder;
import lombok.Getter;

/**
 * INode
 *
 * @author walter
 * @since 1.0
 */
@Builder
@Getter
public class INode {
    private INode ref;

    private Chunk chunk1;
    private Chunk chunk2;
    private Chunk chunk3;
    private Chunk chunk4;
}
