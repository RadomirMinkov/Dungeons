package bg.sofia.uni.fmi.mjt.dungeons.client;

import bg.sofia.uni.fmi.mjt.dungeons.utility.Message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;


public class Client {
    private static final int SERVER_PORT = 7777;
    private static final int BUFFER_SIZE = 1024;
    private static final ByteBuffer BYTE_BUFFER = ByteBuffer.allocate(BUFFER_SIZE);
    private static final String INSTRUCTIONS = """
            map
            help
            login <username> <password>
            logout
            move <direction>
            create character <class>
            delete character <class>
            change character <class>
            create user <username> <password>
            delete user <username> <password>
            exit""";

    private static Message receiveMessage(SocketChannel socketChannel) throws IOException, ClassNotFoundException {
        BYTE_BUFFER.clear();
        socketChannel.read(BYTE_BUFFER);
        BYTE_BUFFER.flip();

        byte[] data = new byte[BYTE_BUFFER.remaining()];
        BYTE_BUFFER.get(data);
        ObjectInputStream out = new ObjectInputStream(new ByteArrayInputStream(data));

        return (Message) out.readObject();
    }

    private static void sendMessage(Message request, SocketChannel socketChannel) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
        outputStream.writeObject(request);
        outputStream.flush();
        byte[] responseData = byteArrayOutputStream.toByteArray();
        BYTE_BUFFER.clear();
        BYTE_BUFFER.put(responseData);
        BYTE_BUFFER.flip();
        socketChannel.write(BYTE_BUFFER);
    }

    public static void main(String[] args) {

        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress("localhost", SERVER_PORT));

            System.out.println("Connected to the server.");
            System.out.println(INSTRUCTIONS);
            while (true) {
                System.out.print("Enter command: ");
                String message = scanner.nextLine(); // read a line from the console

                if ("exit".equals(message)) {
                    break;
                } else if ("help".equals(message)) {
                    System.out.println(INSTRUCTIONS);
                    continue;
                }
                Message request = new Message(message);
                sendMessage(request, socketChannel);
                Message reply = receiveMessage(socketChannel);
                System.out.println(reply);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }
}
