package entity;

import Utility.ByteBufferInputStream;
import biz.Core;
import static entity.WatchDogLogger.log;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

// Non-Blocking Server (Reactor Pattern with Single Thread)
public class Listener implements Runnable {
    private Core core;
    
    private final int PORT;
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    public Listener(int port, Core controller) {
        this.PORT = port;
        this.core = controller;
        
        ServerSocket serverSocket;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocket = serverSocketChannel.socket();
            serverSocket.bind(new InetSocketAddress(PORT));
            selector =  Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException ex) {
            System.err.println("IOException occurs at Listener constructor");
            System.exit(1);
        }
        
        log(Level.INFO, "Listener is up and listening at " + PORT);
    }
    
    @Override
    public void run() {
        do {
            try {
                int numKeys = selector.select();
                if (numKeys > 0) {               
                    Set<SelectionKey> selectedKey = selector.selectedKeys();
                    Iterator<SelectionKey> it = selectedKey.iterator();

                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        if (key.isAcceptable())
                            acceptConnection(key);  
                        if (key.isReadable()) {
                                new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    readFromConn(key);
                                }
                            }).start();
                        }
                    }
                }
            } catch (IOException ex) {
                System.err.println("IOException occurs while selecting at Listener.");
                System.exit(1);
            }
        } while (true);
    }
    
    private void acceptConnection(SelectionKey key){
        Socket socket = null;
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socket = socketChannel.socket();
            socketChannel.register(selector, SelectionKey.OP_READ);
            selector.selectedKeys().remove(key);
        } catch (IOException ex) {
            System.err.println("IOException occurs while accepting connection at Listener.");
            closeSocket(socket);
        }
    }
    
    private void readFromConn(SelectionKey key){
        Socket socket = null;
        try {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1000);
            buffer.clear();
            int numOfByte = socketChannel.read(buffer);
            socket = socketChannel.socket();
            
            if (numOfByte == -1) {
                key.cancel();
                closeSocket(socket);
            } else {
                buffer.flip();
//                Channels.newChannel(System.out).write(buffer);
                core.processData(new ByteBufferInputStream(buffer));
            }
        } catch (IOException ex) {
            System.err.println("IOException occurs while reading from connection.");
            closeSocket(socket);
        }
        
        selector.selectedKeys().remove(key);
    }
    
    private static void closeSocket(Socket socket) {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            System.err.println("Could not close socket at listener.");
        }
    }
}