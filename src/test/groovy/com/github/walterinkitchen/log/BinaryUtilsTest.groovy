package com.github.walterinkitchen.log

import spock.lang.Specification

import java.util.zip.CRC32C

/**
 * Binary utils test
 * @author walter
 * @since 1.0
 */
class BinaryUtilsTest extends Specification {
    def 'int to bytes should output 4 bytes with MSB'() {
        given: 'source'
        int intV = (int) source

        when: 'convert'
        byte[] res = BinaryUtils.int2Bytes(intV)

        then: 'contains 4 bytes'
        res.size() == 4

        then: 'with msb'
        res[0] == ((source >> 24) & 0xff) as byte
        res[1] == ((source >> 16) & 0xff) as byte
        res[2] == ((source >> 8) & 0xff) as byte
        res[3] == ((source >> 0) & 0xff) as byte

        where: 'cases'
        source << [1, 0, 32, 1023, 1212289298, 4294906130, 3755938582, 0xffffffff, 0x7fffffff]
    }

    def 'long to bytes should output 8 bytes with MSB'() {
        given: 'source'
        long longV = (long) source

        when: 'convert'
        byte[] res = BinaryUtils.long2Bytes(longV)

        then: 'contains 8 bytes'
        res.size() == 8

        then: 'with msb'
        res[0] == ((source >> 56) & 0xff) as byte
        res[1] == ((source >> 48) & 0xff) as byte
        res[2] == ((source >> 40) & 0xff) as byte
        res[3] == ((source >> 32) & 0xff) as byte
        res[4] == ((source >> 24) & 0xff) as byte
        res[5] == ((source >> 16) & 0xff) as byte
        res[6] == ((source >> 8) & 0xff) as byte
        res[7] == ((source >> 0) & 0xff) as byte

        where: 'cases'
        source << [0, 1l, 100l, 2000l, 2893562764341482262, 0xffffffff, 0xffffffffffffffff]
    }

    def 'crc32 should return correct crc value'() {
        given: 'source'
        byte[] bytes = source as byte[]

        when: 'calculate crc'
        long crc = BinaryUtils.crc32(bytes)

        then: 'crc should matched'
        crc == calculateCrc(bytes)

        where: 'cases'
        source << [[], [0], [1, 2, 3], [0xff, 0x01, 22, 33, 21, 99, 0x21, 0x7f]]
    }

    long calculateCrc(byte[] bytes) {
        def crcc = new CRC32C()
        crcc.update(bytes, 0, bytes.size())
        return crcc.getValue()
    }
}
