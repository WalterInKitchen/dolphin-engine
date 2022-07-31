package com.github.walterinkitchen.log

import spock.lang.Specification

/**
 * Imap test
 * @author walter
 * @since 1.0
 */
class IMapTest extends Specification {
    def 'when nodes is null throw exception'() {
        given: 'nothing'
        when: 'build with null'
        IMap.build(0, null);

        then: 'throw illegalArgumentException'
        thrown(IllegalArgumentException)
    }

    def 'when address < 0 throw exception'() {
        given: 'nothing'
        when: 'build with null'
        IMap.build(-1, Collections.emptyMap());

        then: 'throw illegalArgumentException'
        thrown(IllegalArgumentException)
    }

    def 'when nodes contains null key throw exception'() {
        given: 'nothing'
        when: 'build with null'
        IMap.build(1, Collections.singletonMap(null, 1000L));

        then: 'illegalArgumentException'
        thrown(IllegalArgumentException)
    }

    def 'when nodes contains null value throw exception'() {
        given: 'nothing'
        when: 'build with null'
        IMap.build(1, Collections.singletonMap(20L, null));

        then: 'illegalArgumentException'
        thrown(IllegalArgumentException)
    }

    def 'when nodes is empty then payload size = fixedPart size'() {
        given: 'nothing'
        when: 'build with empty nodes'
        def imap = IMap.build(10, Collections.emptyMap())

        then: 'payload size = fixed parse size'
        imap.payloadSize() == 4
        imap.binarySize() == 16
    }

    def 'when nodes not empty then binary size = payloadSize + header + tail'() {
        given: 'nothing'
        when: 'build with empty nodes'
        def imap = IMap.build(adder as long, nodesMap as Map<Long, Long>)

        then: 'binary size= payloadSize+header+tail'
        imap.binarySize() == imap.payloadSize() + 4 + 8

        where: 'cases'
        adder | nodesMap
        10    | [100L: 10L]
        15    | [1000L: 210L, 200L: 10L]
        15    | [1000L: 210L, 200L: 10L, 0L: 0L]
        15    | [1000L: 210L, 200L: 10L, 0L: 0L, 01L: 0L]
    }

    def 'when nodes is not empty then payload size = fixedPart + nodesSize'() {
        given: 'nothing'
        when: 'build with empty nodes'
        def imap = IMap.build(adder as long, nodesMap as Map<Long, Long>)

        then: 'payload size = fixed + nodesSize'
        imap.address() == adder
        imap.payloadSize() == 4 + nodesMap.size() * 8 * 2

        where: 'cases'
        adder | nodesMap
        10    | [100L: 10L]
        15    | [1000L: 210L, 200L: 10L]
        15    | [1000L: 210L, 200L: 10L, 0L: 0L]
        15    | [1000L: 210L, 200L: 10L, 0L: 0L, 01L: 0L]
    }

    def 'payload = nodesSize + nodesId + nodesAdder'() {
        given: 'nothing'
        when: 'build with empty nodes'
        def imap = IMap.build(adder as long, nodesMap as Map<Long, Long>)

        byte[] nodeSize = BinaryUtils.int2Bytes(nodesMap.size())
        byte[] nodeIds = nodesMap.keySet().collect { it -> BinaryUtils.long2Bytes(it) }.flatten() as byte[]
        byte[] nodeValues = nodesMap.values().collect { it -> BinaryUtils.long2Bytes(it) }.flatten() as byte[]

        then: 'payload = fixed + nodesSize'
        imap.payload() == [nodeSize, nodeIds, nodeValues].flatten() as byte[]

        where: 'cases'
        adder | nodesMap
        10    | [100L: 10L]
        15    | [1000L: 210L, 200L: 10L]
        15    | [1000L: 210L, 200L: 10L, 0L: 0L]
        15    | [1000L: 210L, 200L: 10L, 0L: 0L, 01L: 0L]
    }

    def 'crc = crc32(header,payload)'() {
        given: 'nothing'
        when: 'build with empty nodes'
        def imap = IMap.build(adder as long, nodesMap as Map<Long, Long>)

        then: 'crc= crc32(header+payload)'
        imap.crc() == BinaryUtils.crc32(imap.toBinary(), 0, imap.binarySize() - 8)

        where: 'cases'
        adder | nodesMap
        10    | [100L: 10L]
        15    | [1000L: 210L, 200L: 10L]
        15    | [1000L: 210L, 200L: 10L, 0L: 0L]
        15    | [1000L: 210L, 200L: 10L, 0L: 0L, 01L: 0L]
    }

    def 'binary = header + payload + tail'() {
        given: 'nothing'
        when: 'build with empty nodes'
        def imap = IMap.build(adder as long, nodesMap as Map<Long, Long>)

        def header = BinaryUtils.int2Bytes(imap.payloadSize())
        def tail = BinaryUtils.long2Bytes(imap.crc())

        then: 'payload = fixed + nodesSize'
        imap.toBinary() == [header, imap.payload(), tail].flatten() as byte[]

        where: 'cases'
        adder | nodesMap
        10    | [100L: 10L]
        15    | [1000L: 210L, 200L: 10L]
        15    | [1000L: 210L, 200L: 10L, 0L: 0L]
        15    | [1000L: 210L, 200L: 10L, 0L: 0L, 01L: 0L]
    }
}
