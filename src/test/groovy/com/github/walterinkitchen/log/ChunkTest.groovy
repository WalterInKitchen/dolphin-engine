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
            size() == expectedSize
            address() == 0
            crc() == BinaryUtils.crc32(bytes)
        }

        where: 'cases'
        source                      | expectedSize
        []                          | 0 + 12
        [3]                         | 1 + 12
        [1, 2, 3]                   | 3 + 12
        [1, 2, 3, 4, 5, 6, 7, 8, 9] | 9 + 12
    }

}
