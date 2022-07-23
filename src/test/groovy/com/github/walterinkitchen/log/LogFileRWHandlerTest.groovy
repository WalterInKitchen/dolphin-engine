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
