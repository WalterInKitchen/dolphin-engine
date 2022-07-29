package com.github.walterinkitchen.log

import spock.lang.Specification

/**
 * INodeTest
 * @author walter
 * @since 1.0
 */
class INodeTest extends Specification {
    def 'inode should return correct basic properties'() {
        given: 'iNode'
        when: 'build node'
        INode node = INode.build(id as long, chunks as long[], refAdder as long)

        then: 'base property should correct'
        with(node) {
            getId() == id
            getRefAdder() == refAdder
            getChunkSize() == chunks.size()
            getChunkAddress() == chunks as long[]
        }

        where: 'cases'
        id  | chunks          | refAdder
        100 | [100, 200, 300] | 100L
        20  | [10, 300]       | 10L
    }

    def 'iNode binary size=sizeOf(payload)+sizeof(nonePayload)'() {
        given: 'iNode'
        when: 'build node'
        INode node = INode.build(0l, chunks as long[], 1l)

        def expectedPayloadSize = 8 + 4 + chunks.size() * 8 + 8
        def expectedSize = expectedPayloadSize + 4 + 8

        then: 'binarySize is correct'
        with(node) {
            binarySize() == expectedSize
            payloadSize() == expectedPayloadSize
        }

        where: 'cases'
        chunks << [[], [0], [1, 2, 3, 4], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]]
    }

    def 'iNode payload = id+chunkSize+chunks+refAdder'() {
        given: 'iNode'
        when: 'build node'
        INode node = INode.build(id as long, chunks as long[], refAdder as long)

        byte[] expectedPayload = [BinaryUtils.long2Bytes(id),
                                  BinaryUtils.int2Bytes(chunks.size()),
                                  chunks.collect { it -> BinaryUtils.long2Bytes(it as long) },
                                  BinaryUtils.long2Bytes(refAdder as long)]
                .flatten() as byte[]


        then: 'payload matched'
        with(node) {
            payload() == expectedPayload
        }

        where: 'cases'
        id                 | refAdder           | chunks
        0                  | 1                  | []
        0                  | 1                  | [0x01]
        0x1f2f3f4f5f6f7f8f | 0xf1f2f3f4f5f6f7f8 | [0xa1a2a3a4a5a6a7a8, 0xb1b2b3b4b5b6b7b8]
    }

    def 'binary bytes = payloadSize + payload[] + crc'() {
        given: 'iNode'
        when: 'build node'
        INode node = INode.build(id as long, chunks as long[], refAdder as long)

        def expectedBinary = [BinaryUtils.int2Bytes(node.payloadSize()),
                              node.payload(),
                              BinaryUtils.long2Bytes(node.crc())]
                .flatten() as byte[]

        then: 'binary matched'
        with(node) {
            toBinary() == expectedBinary
        }

        where: 'cases'
        id                 | refAdder           | chunks
        0                  | 1                  | []
        0                  | 1                  | [0x01]
        0x1f2f3f4f5f6f7f8f | 0xf1f2f3f4f5f6f7f8 | [0xa1a2a3a4a5a6a7a8, 0xb1b2b3b4b5b6b7b8]
    }

    def 'crc=crc32(payloadSize to lastChunkAddress)'() {
        given: 'iNode'
        when: 'build node'
        INode node = INode.build(id as long, chunks as long[], refAdder as long)

        byte[] binary = node.toBinary()
        def expectedCrc = BinaryUtils.crc32(binary, 0, binary.size() - 8)

        then: 'payload matched'
        with(node) {
            crc() == expectedCrc
        }

        where: 'cases'
        id                 | refAdder           | chunks
        0                  | 1                  | []
        0                  | 1                  | [0x01]
        0x1f2f3f4f5f6f7f8f | 0xf1f2f3f4f5f6f7f8 | [0xa1a2a3a4a5a6a7a8, 0xb1b2b3b4b5b6b7b8]
    }
}
