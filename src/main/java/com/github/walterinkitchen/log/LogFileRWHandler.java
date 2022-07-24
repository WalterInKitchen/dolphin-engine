package com.github.walterinkitchen.log;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Logfile reader and writer
 *
 * @author walter
 * @since 1.0
 */
public class LogFileRWHandler implements BinaryReader, BinaryWriter, Closeable {
    private final RwBinaryFile file;

    /**
     * create a handler
     *
     * @param file channel
     */
    protected LogFileRWHandler(RwBinaryFile file) {
        this.file = file;
    }

    @Override
    public ByteBuffer read(long start, int size) throws IOException {
        if (start < 0 || size <= 0) {
            throw new IllegalArgumentException();
        }
        ByteBuffer buffer = ByteBuffer.allocate(size);
        file.channel().read(buffer, start);
        return buffer;
    }

    @Override
    public void write(long start, ByteBuffer buffer) throws IOException {
        if (start < 0) {
            throw new IllegalArgumentException();
        }
        file.channel().write(buffer, start);
    }

    @Override
    public void write(long start, byte[] bytes) throws IOException {
        if (start < 0 || bytes == null) {
            throw new IllegalArgumentException();
        }
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        write(start, buffer);
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        long start = size();
        write(start, bytes);
    }

    @Override
    public void write(ByteBuffer buffer) throws IOException {
        long start = size();
        write(start, buffer);
    }

    @Override
    public long size() {
        return file.size();
    }

    @Override
    public void close() {
        file.close();
    }
}
