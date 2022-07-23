package com.github.walterinkitchen.log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * read write Binary file
 *
 * @author walter
 * @since 1.0
 */
public class RwBinaryFile {
    private final Path path;
    private volatile FileChannel channel;

    /**
     * constructor
     *
     * @param path path
     */
    public RwBinaryFile(Path path) {
        this.path = path;
    }

    /**
     * get read write channel
     *
     * @return channel instance
     */
    public FileChannel channel() {
        return getChannel();
    }

    /**
     * close
     */
    public void close() {
        IOUtils.closeQuietly(this.channel);
    }

    private FileChannel getChannel() {
        if (this.channel == null) {
            synchronized (this) {
                if (this.channel == null) {
                    this.channel = createChannel();
                }
            }
        }
        return this.channel;
    }

    private FileChannel createChannel() {
        try {
            return FileChannel.open(this.path, StandardOpenOption.WRITE, StandardOpenOption.READ);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
