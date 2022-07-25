package com.github.walterinkitchen.log

import spock.lang.Specification

/**
 * Chunk Test
 * @author walter
 * @since 1.0
 */
class ChunkTest extends Specification {

    def 'chunk should with right payload size and payload'() {
        given: 'source'
        byte[] bytes = source as byte[]

        when: 'get property'
        Chunk chunk = Chunk.build(bytes);

        then: 'with collect size'
        with(chunk) {
            payload() == bytes
            payloadSize() == bytes.size()
            binarySize() == expectedSize
            address() == -1
            crc() == BinaryUtils.crc32(payloadSize(), payload())
        }

        where: 'cases'
        source                      | expectedSize
        []                          | 0 + 12
        [3]                         | 1 + 12
        [1, 2, 3]                   | 3 + 12
        [1, 2, 3, 4, 5, 6, 7, 8, 9] | 9 + 12
    }

    def 'chunks binary should contains header+payload+crc'() {
        given: 'source'
        byte[] bytes = source as byte[]

        when: 'to binary'
        Chunk chunk = Chunk.build(bytes);
        byte[] res = chunk.toBinary()

        then: 'size matched'
        res.size() == chunk.binarySize()

        def expected = buildChunkBinaryBytes(bytes)
        then: 'content matched'
        res == expected

        where: 'cases'
        source << [[], [0], [1, 2, 3], [1, 2, 3, 4, 5, 6, 7, 8]]
    }

    byte[] buildChunkBinaryBytes(byte[] src) {
        byte[] header = BinaryUtils.int2Bytes(src.size())
        long crc = BinaryUtils.crc32(src.length, src)
        byte[] tail = BinaryUtils.long2Bytes(crc)
        return [header, src, tail].flatten() as byte[]
    }
}
