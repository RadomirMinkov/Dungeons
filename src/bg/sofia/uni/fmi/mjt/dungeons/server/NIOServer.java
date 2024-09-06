package bg.sofia.uni.fmi.mjt.dungeons.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    private Selector selector;
    private ServerSocketChannel serverChannel;
    private ByteBuffer buffer;

    public NIOServer(String host, int port) throws IOException {
        selector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.bind(new InetSocketAddress(host, port));

        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        buffer = ByteBuffer.allocate(1024);

        System.out.println("Server started on port " + port);
    }

    public void start() throws IOException {
        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove();

                if (key.isAcceptable()) {
                    handleAccept(key);
                } else if (key.isReadable()) {
                    handleRead(key);
                } else if (key.isWritable()) {
                    handleWrite(key);
                }
            }
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);

        System.out.println("Client connected: " + clientChannel.getRemoteAddress());

        clientChannel.register(selector, SelectionKey.OP_READ);
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        buffer.clear();

        int bytesRead = clientChannel.read(buffer);
        if (bytesRead == -1) {
            System.out.println("Client has disconnected: " + clientChannel.getRemoteAddress());
            closeClientConnection(key);
            return;
        }

        buffer.flip();
        String messageFromClient = new String(buffer.array(), 0, bytesRead);
        System.out.println("Received from client: " + messageFromClient);

        // Echo the message back to the client
        key.interestOps(SelectionKey.OP_WRITE);
        key.attach(messageFromClient);
    }

    private void handleWrite(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        String message = (String) key.attachment();

        if (message != null) {
            ByteBuffer buffer = ByteBuffer.wrap(("Echo: " + message).getBytes());
            clientChannel.write(buffer);
            System.out.println("Sent to client: " + message);

            key.interestOps(SelectionKey.OP_READ);
        }
    }

    public void closeClientConnection(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        System.out.println("Closing client connection: " + clientChannel.getRemoteAddress());

        clientChannel.close();

        key.cancel();
    }

    public void shutdown() throws IOException {
        System.out.println("Shutting down the server...");
        selector.close();
        serverChannel.close();
    }

    public static void main(String[] args) {
        try {
            NIOServer server = new NIOServer("localhost", 7777);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
