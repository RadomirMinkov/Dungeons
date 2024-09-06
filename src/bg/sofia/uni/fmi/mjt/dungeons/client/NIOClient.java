package bg.sofia.uni.fmi.mjt.dungeons.client;

import bg.sofia.uni.fmi.mjt.dungeons.gamelogic.Mode;
import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    private static Mode currentMode;
    private static final int BUFFER_SIZE = 1024;
    private boolean running = true;
    static final String NORMAL_MODE_INSTRUCTIONS = """
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

    static final String BATTLE_MODE_INSTRUCTIONS = """
            attack
            defend
            power up
            use potion <potion-type> { health, mana }""";
    static final String TRADING_MODE_INSTRUCTIONS = """
            offer <item>
            accept item
            exit""";
    static final String TREASURE_MODE_INSTRUCTIONS = """
            pick up
            put down
            exit""";
    static final String CHOOSE_MODE_INSTRUCTIONS = """
            trade
            fight
            nothing
            exit""";

    static {
        currentMode = Mode.NORMAL;
    }

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

        while (running) {
            int keyNumber = selector.select();
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
        if (message.equals("help")) {
            System.out.println(NORMAL_MODE_INSTRUCTIONS);
            return;
        }
        clientChannel.register(selector, SelectionKey.OP_WRITE);

        clientChannel.keyFor(selector).attach(message);
        selector.wakeup();
    }

    private void handleWrite(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        String message = (String) key.attachment();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
        outputStream.writeObject(new Message(message, Mode.NORMAL));
        outputStream.flush();

        byte[] serializedData = byteArrayOutputStream.toByteArray();

        ByteBuffer buffer = ByteBuffer.allocate(serializedData.length);
        buffer.put(serializedData);
        buffer.flip();

        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
        key.interestOps(SelectionKey.OP_READ);
    }

    public void closeConnection() throws IOException {
        System.out.println("Closing connection...");
        if (clientChannel != null) {
            clientChannel.close();
        }
        if (selector != null) {
            selector.close();
        }
        running = false;
        System.out.println("Connection closed.");
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
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        ObjectInputStream out = new ObjectInputStream(new ByteArrayInputStream(data));
        try {
            Message message = (Message) out.readObject();
            System.out.println(message.message());
            if (currentMode != message.mode()) {
                System.out.println("You entered " + message.mode());
                currentMode = message.mode();
                printModeInstructions(message.mode());
            }
            System.out.print("Enter command: ");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void printModeInstructions(Mode mode) {
        switch (mode) {
            case NORMAL -> System.out.println(NORMAL_MODE_INSTRUCTIONS);
            case TRADE -> System.out.println(TRADING_MODE_INSTRUCTIONS);
            case BATTLE -> System.out.println(BATTLE_MODE_INSTRUCTIONS);
            case TREASURE -> System.out.println(TREASURE_MODE_INSTRUCTIONS);
            case CHOOSE -> System.out.println(CHOOSE_MODE_INSTRUCTIONS);
        }
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