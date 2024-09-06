package bg.sofia.uni.fmi.mjt.dungeons.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.SERVER_HOST;
import static bg.sofia.uni.fmi.mjt.dungeons.utility.Constants.SERVER_PORT;

public class NIOClient {

    private Selector selector;
    private SocketChannel clientChannel;
    private ByteBuffer buffer;

    private static final int BUFFER_SIZE = 1024;
    private static final String NORMAL_MODE_INSTRUCTIONS = """
            map
            help
            login <username> <password>
            logout
            move <direction>
            create character <class>
            delete character <class>
            choose/change character <class>
            create user <username> <password>
            delete user <username> <password>
            exit""";

    private static final String BATTLE_MODE_INSTRUCTIONS = """
            attack
            defend
            power up
            use potion <potion-type> { health, mana }
            exit""";
    private static final String TRADING_MODE_INSTRUCTIONS = """
            share <item>
            accept trade
            accept item
            exit""";
    private static final String TREASURE_MODE_INSTRUCTIONS = """
            pick up
            put down
            exit""";

    public NIOClient(String host, int port) throws IOException {
        selector = Selector.open();
        clientChannel = SocketChannel.open();
        clientChannel.configureBlocking(false);
        clientChannel.connect(new InetSocketAddress(host, port));

        clientChannel.register(selector, SelectionKey.OP_CONNECT);
        buffer = ByteBuffer.allocate(BUFFER_SIZE);
    }

    public void start() throws IOException {
        new Thread(new ConsoleInputHandler(this)).start();

        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove();

                if (key.isConnectable()) {
                    handleConnect(key);
                } else if (key.isWritable()) {
                    handleWrite(key);
                } else if (key.isReadable()) {
                    handleRead(key);
                }
            }
        }
    }

    private void handleConnect(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();

        if (channel.isConnectionPending()) {
            channel.finishConnect();
        }

        System.out.println("Connected to server!");

        channel.register(selector, SelectionKey.OP_READ);
    }

    public void sendMessage(String message) throws IOException {
        clientChannel.register(selector, SelectionKey.OP_WRITE);

        clientChannel.keyFor(selector).attach(message);
    }

    private void handleWrite(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();

        String message = (String) key.attachment();
        if ("exit".equals(message)) {
            break;
        } else if ("help".equals(message)) {
            System.out.println(BATTLE_MODE_INSTRUCTIONS);
            continue;
        }

        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
        channel.write(buffer);

        System.out.println("Message sent to server: " + message);

        key.interestOps(SelectionKey.OP_READ);
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();

        buffer.clear();
        int bytesRead = channel.read(buffer);

        if (bytesRead == -1) {
            channel.close();
            key.cancel();
            return;
        }

        buffer.flip();
        String messageFromServer = new String(buffer.array(), 0, bytesRead);
        System.out.println("Received from server: " + messageFromServer);
    }

    public static void main(String[] args) {
        try {
            NIOClient client = new NIOClient(SERVER_HOST, SERVER_PORT);
            client.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}