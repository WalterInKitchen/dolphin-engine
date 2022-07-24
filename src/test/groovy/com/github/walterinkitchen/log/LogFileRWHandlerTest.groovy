package com.github.walterinkitchen.log

import org.apache.commons.io.FileUtils
import spock.lang.Specification

import java.nio.ByteBuffer
import java.nio.file.Path

/**
 * test
 * @author walter
 * @since 1.0
 */
class LogFileRWHandlerTest extends Specification {

    def 'write bytes should equal to readout'() {
        given: 'test file and handler'
        File file = File.createTempFile('temp', 'data');
        def path = Path.of(file.getPath())
        LogFileRWHandler handler = new LogFileRWHandler(new RwBinaryFile(path));

        when: 'write source to start'
        handler.write(start, source as byte[])

        def res = handler.read(start, source.size());
        then: 'read from start should eq to source'
        res.array() == source as byte[]

        cleanup: 'clean'
        FileUtils.deleteQuietly(file);

        where: 'given start and content'
        start | source
        0     | [0x01, 0x02, 0x03]
        1     | [0x01, 0x02, 0x03]
        2     | [0x01, 0x02, 0x03]
        3     | [0x02, 0x01, 0x00]
        30    | [0xff, 0xff, 0xff]
        300   | [0xff, 0xff, 0xff, 12]
        100   | [0, 0, 0]
    }

    def 'write bytebuffer should eq to readout'() {
        given: 'test file and handler'
        File file = File.createTempFile('temp', 'data')
        def path = Path.of(file.getPath())
        LogFileRWHandler handler = new LogFileRWHandler(new RwBinaryFile(path));

        when: 'write source to start'
        def buffer = ByteBuffer.allocate(source.size())
        buffer.put(source as byte[])
        buffer.flip();
        handler.write(start, buffer)

        def res = handler.read(start, source.size());
        then: 'read from start should eq to source'
        with(res) {
            array() == source as byte[]
            array().size() == source.size()
        }

        cleanup: 'clean'
        FileUtils.deleteQuietly(file);

        where: 'given start and content'
        start | source
        0     | [0x01, 0x02, 0x03]
        1     | [0x01, 0x02, 0x03]
        2     | [0x01, 0x02, 0x03]
        3     | [0x02, 0x01, 0x00]
        30    | [0xff, 0xff, 0xff]
        300   | [0xff, 0xff, 0xff, 12]
        100   | [0, 0, 0]
    }

    def 'write bytes without start should write to end'() {
        given: 'test file and handler'
        File file = File.createTempFile('temp', 'data')
        def path = Path.of(file.getPath())
        LogFileRWHandler handler = new LogFileRWHandler(new RwBinaryFile(path));

        when: 'write source to start'
        handler.write([1, 2, 3] as byte[])
        handler.write([4, 5, 6] as byte[])
        handler.write([7, 8, 9] as byte[])

        def res = handler.read(0, (int) handler.size())
        then: 'res should be '
        with(res) {
            array().size() == 9
            array() == [1, 2, 3, 4, 5, 6, 7, 8, 9] as byte[]
        }
    }

    def 'write buffer without start should write to end'() {
        given: 'test file and handler'
        File file = File.createTempFile('temp', 'data')
        def path = Path.of(file.getPath())
        LogFileRWHandler handler = new LogFileRWHandler(new RwBinaryFile(path));

        when: 'write source to start'
        handler.write(ByteBuffer.allocate(3).put([1, 2, 3] as byte[]).flip())
        handler.write(ByteBuffer.allocate(3).put([4, 5, 6] as byte[]).flip())
        handler.write(ByteBuffer.allocate(3).put([7, 8, 9] as byte[]).flip())

        def res = handler.read(0, (int) handler.size())
        then: 'res should be '
        with(res) {
            array().size() == 9
            array() == [1, 2, 3, 4, 5, 6, 7, 8, 9] as byte[]
        }
    }

    def 'close should close channel'() {
        given: 'channel'
        RwBinaryFile file = Mock()
        LogFileRWHandler handler = new LogFileRWHandler(file)

        when: 'close log file'
        handler.close()

        then: 'binary file close'
        1 * file.close()
    }
}
