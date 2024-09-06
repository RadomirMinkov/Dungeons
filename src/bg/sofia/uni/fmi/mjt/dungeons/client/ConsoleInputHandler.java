package bg.sofia.uni.fmi.mjt.dungeons.client;

import java.io.IOException;
import java.util.Scanner;

import static bg.sofia.uni.fmi.mjt.dungeons.client.NIOClient.NORMAL_MODE_INSTRUCTIONS;

public class ConsoleInputHandler implements Runnable {

    private NIOClient client;

    public ConsoleInputHandler(NIOClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(NORMAL_MODE_INSTRUCTIONS);
        while (true) {

            System.out.print("Enter command: ");
            String message = scanner.nextLine();

            if (message.equalsIgnoreCase("exit")) {
                try {
                    client.closeConnection();
                    break;
                } catch (IOException e) {
                    System.out.println("You closed connection!");
                }
            } else {
                try {
                    client.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}