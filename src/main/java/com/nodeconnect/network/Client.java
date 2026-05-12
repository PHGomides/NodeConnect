package main.java.com.nodeconnect.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public void main() {
        try (
                Socket socket = new Socket("localhost", 6379);
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Servidor ON");


            Thread.startVirtualThread(() -> {
                try {
                    while (true) {
                        int length = in.readInt();
                        if (length > 0) {
                            byte[] messageBytes = new byte[length];
                            in.readFully(messageBytes, 0, length);
                            String message = new String(messageBytes);
                            System.out.println("\n[Servidor]: " + message);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("\nA ligação ao servidor foi perdida.");
                }
            });


            while (true) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("/sair")) {
                    break;
                }

                byte[] messageBytes = input.getBytes();


                out.writeInt(messageBytes.length);
                out.write(messageBytes);
                out.flush();
            }

            System.out.println("Encerrando a ligação...");

        } catch (IOException e) {
            System.out.println("Erro no cliente: " + e.getMessage());
        }
    }
}
