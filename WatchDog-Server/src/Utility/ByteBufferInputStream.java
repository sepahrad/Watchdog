package Utility;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferInputStream extends InputStream {
    private ByteBuffer buffer;
    
    public ByteBufferInputStream(ByteBuffer buffer) {
        this.buffer = buffer;
    }
    
    public int read() throws IOException {
        if (buffer.hasRemaining())
            return buffer.get();
        else
            return -1;
    }
}
