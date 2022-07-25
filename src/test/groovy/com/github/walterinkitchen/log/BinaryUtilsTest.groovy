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

    def 'int to bytes with target array should set bytes at offset'() {
        given: 'source'
        int intV = (int) source

        byte[] array = new byte[100]
        when: 'convert'
        byte[] res = BinaryUtils.int2Bytes(intV, array, offset)

        then: 'output same with source'
        res == array

        then: 'with msb'
        res[0 + offset] == ((source >> 24) & 0xff) as byte
        res[1 + offset] == ((source >> 16) & 0xff) as byte
        res[2 + offset] == ((source >> 8) & 0xff) as byte
        res[3 + offset] == ((source >> 0) & 0xff) as byte

        where: 'cases'
        source     | offset
        0          | 0
        20         | 1
        32         | 96
        0xffffffff | 96
        -1         | 30
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

    def 'long to bytes with target array should set value at offset'() {
        given: 'source'
        long longV = (long) source

        when: 'convert'
        byte[] array = new byte[100]
        byte[] res = BinaryUtils.long2Bytes(longV, array, offset)

        then: 'output same with source'
        res == array

        then: 'with msb'
        res[0 + offset] == ((source >> 56) & 0xff) as byte
        res[1 + offset] == ((source >> 48) & 0xff) as byte
        res[2 + offset] == ((source >> 40) & 0xff) as byte
        res[3 + offset] == ((source >> 32) & 0xff) as byte
        res[4 + offset] == ((source >> 24) & 0xff) as byte
        res[5 + offset] == ((source >> 16) & 0xff) as byte
        res[6 + offset] == ((source >> 8) & 0xff) as byte
        res[7 + offset] == ((source >> 0) & 0xff) as byte

        where: 'cases'
        source | offset
        0      | 0
        100l   | 80
        2000l  | 90
        2000l  | 92
    }

    def 'crc32 with int and bytes should return correct crc'() {
        given: 'source'
        byte[] bytes = source as byte[]

        when: 'calculate crc'
        def res = BinaryUtils.crc32(intV as int, bytes)

        then: 'crc matched'
        res == calculateCrc(intV, bytes, 0, bytes.length)

        where: 'cases'
        intV  | source
        22    | []
        0     | [33, 22, 11, 0]
        11111 | [22, 11, 0]
    }

    def 'crc32 should return correct crc value'() {
        given: 'source'
        byte[] bytes = source as byte[]

        when: 'calculate crc'
        long crc = BinaryUtils.crc32(bytes)

        then: 'crc should matched'
        crc == calculateCrc(bytes, 0, bytes.length)

        where: 'cases'
        source << [[], [0], [1, 2, 3], [0xff, 0x01, 22, 33, 21, 99, 0x21, 0x7f]]
    }

    def 'crc32 with offset and length should return correct crc value'() {
        given: 'source'
        byte[] bytes = source as byte[]

        when: 'calculate crc'
        long crc = BinaryUtils.crc32(bytes, offset, length)

        then: 'crc should matched'
        crc == calculateCrc(bytes, offset, length)

        where: 'cases'
        source       | offset | length
        []           | 0      | 0
        [0]          | 0      | 0
        [0]          | 0      | 1
        [1, 2, 3, 4] | 1      | 2
    }

    long calculateCrc(byte[] bytes, int offset, int len) {
        def crcc = new CRC32C()
        crcc.update(bytes, offset, len)
        return crcc.getValue()
    }

    long calculateCrc(int intV, byte[] bytes, int offset, int len) {
        def crcc = new CRC32C()
        crcc.update(intV);
        crcc.update(bytes, offset, len)
        return crcc.getValue()
    }
}
