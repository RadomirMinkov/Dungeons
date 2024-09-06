package bg.sofia.uni.fmi.mjt.dungeons.client;

import java.io.IOException;
import java.util.Scanner;

public class ConsoleInputHandler implements Runnable {

    private NIOClient client;

    public ConsoleInputHandler(NIOClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter message: ");
            String message = scanner.nextLine();
            try {
                client.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}