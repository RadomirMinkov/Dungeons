package bg.sofia.uni.fmi.mjt.dungeons.server;

import bg.sofia.uni.fmi.mjt.dungeons.command.interpreter.CommandInterpreter;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.MapElementAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.SERVER_HOST;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.SERVER_PORT;

public class Server {
    private static final int BUFFER_SIZE = 1024;
    private static CommandInterpreter commandInterpreter;
    private static List<SelectionKey> selectionKeys;

    static {
        try {
            commandInterpreter = new CommandInterpreter();
        } catch (MapElementAlreadyExistsException e) {
            System.out.println("Failed initialisation of the map!");
        }
        selectionKeys = new ArrayList<>();
    }

    private static Message readRequest(ByteBuffer buffer)
            throws IOException, ClassNotFoundException {
        buffer.flip();

        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        return (Message) ois.readObject();
    }

    private static void sendMessage(Message response, ByteBuffer byteBuffer, SocketChannel sc) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
        outputStream.writeObject(response);
        outputStream.flush();
        byte[] responseData = byteArrayOutputStream.toByteArray();
        byteBuffer.clear();
        byteBuffer.put(responseData);
        byteBuffer.flip();
        sc.write(byteBuffer);
    }

    public static void main(String[] args) {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            serverSocketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            while (true) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                processClients(buffer, keyIterator, selector);

            }

        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the server socket", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void processClients(ByteBuffer buffer, Iterator<SelectionKey> keyIterator, Selector selector)
            throws IOException, ClassNotFoundException {
        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();
            if (!selectionKeys.contains(key)) {
                selectionKeys.add(key);
            }
            if (key.isReadable()) {
                SocketChannel sc = (SocketChannel) key.channel();
                buffer.clear();
                int r = sc.read(buffer);
                if (r < 0) {
                    System.out.println("Client has closed the connection");
                    sc.close();
                    continue;
                }
                Message message = readRequest(buffer);
                Message response = commandInterpreter.executeCommand(message, key);
                sendMessage(response, buffer, sc);

            } else if (key.isAcceptable()) {
                ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
                SocketChannel accept = sockChannel.accept();
                accept.configureBlocking(false);
                accept.register(selector, SelectionKey.OP_READ);
            } else if (key.isWritable()) {
                SocketChannel sc = (SocketChannel) key.channel();
            }
            keyIterator.remove();
        }
    }
}
