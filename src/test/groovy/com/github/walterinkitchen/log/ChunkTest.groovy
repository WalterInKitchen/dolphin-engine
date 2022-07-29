package com.github.walterinkitchen.log

import com.github.walterinkitchen.exception.IntegrityBrokenError
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
        Chunk chunk = Chunk.build(-1, bytes);

        then: 'with collect size'
        with(chunk) {
            payload() == bytes
            payloadSize() == bytes.size()
            binarySize() == expectedSize
            address() == -1
            crc() == BinaryUtils.crc32([BinaryUtils.int2Bytes(payloadSize()), payload()].flatten() as byte[])
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
        Chunk chunk = Chunk.build(-1, bytes);
        byte[] res = chunk.toBinary()

        then: 'size matched'
        res.size() == chunk.binarySize()

        def expected = buildChunkBinaryBytes(bytes)
        then: 'content matched'
        res == expected

        where: 'cases'
        source << [[], [0], [1, 2, 3], [1, 2, 3, 4, 5, 6, 7, 8]]
    }

    def 'build chunk from binary should return instance with correct properties'() {
        given: 'source'
        byte[] bytes = source as byte[]
        def original = Chunk.build(-1, bytes)
        def binary = original.toBinary()

        when: 'build instance from binary'
        def instance = Chunk.fromBinary(address, binary)

        then: 'address matched'
        instance != null
        instance.address() == address

        then: 'to binary should eq to the original binary'
        instance.toBinary() == binary

        where: 'cases'
        address    | source
        0          | []
        11         | [1, 2, 3]
        0xffffffff | [0, 1, 2, 3, 4, 5, 6]
    }

    def 'build chunk from binary should throw exception if binary size less than non-payload info size'() {
        given: 'source'
        byte[] binary = source as byte[]

        when: 'build instance from binary'
        Chunk.fromBinary(0l, binary)

        then: 'should throw exception'
        def e = thrown(IntegrityBrokenError)
        e.message == 'binary is broken'

        where: 'cases'
        source << [[], [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]]
    }

    def 'build chunk throw exception if payload not long enough'() {
        given: 'source'
        byte[] binary = [payloadSize, payload, crc].flatten() as byte[]

        when: 'build instance from binary'
        Chunk.fromBinary(0l, binary)

        then: 'should throw exception'
        def e = thrown(IntegrityBrokenError)
        e.message == 'binary is broken,binary not long enough'

        where: 'cases that payloadSize will make array not long enough'
        payloadSize  | payload   | crc
        [0, 0, 0, 1] | []        | [0, 0, 0, 0, 0, 0, 0, 0]
        [0, 0, 0, 2] | [0]       | [0, 0, 0, 0, 0, 0, 0, 0]
        [0, 0, 0, 3] | [0, 0]    | [0, 0, 0, 0, 0, 0, 0, 0]
        [0, 0, 0, 4] | [0, 0, 0] | [0, 0, 0, 0, 0, 0, 0, 0]
    }

    def 'build chunk throw exception if crc not match'() {
        given: 'source'
        byte[] binary = [BinaryUtils.int2Bytes(payloadSize), payload, BinaryUtils.long2Bytes(crc)].flatten() as byte[]

        when: 'build instance from binary'
        Chunk.fromBinary(0l, binary)

        then: 'should throw exception'
        def e = thrown(IntegrityBrokenError)
        e.message == 'binary is broken,crc not matched'

        where: 'cases that payloadSize will make array not long enough'
        payloadSize | payload | crc
        0           | []      | 0L
        1           | [20]    | 0L
        1           | [20, 2] | 0L
    }

    byte[] buildChunkBinaryBytes(byte[] src) {
        byte[] header = BinaryUtils.int2Bytes(src.size())
        long crc = BinaryUtils.crc32([BinaryUtils.int2Bytes(src.size()), src].flatten() as byte[])
        byte[] tail = BinaryUtils.long2Bytes(crc)
        return [header, src, tail].flatten() as byte[]
    }
}
