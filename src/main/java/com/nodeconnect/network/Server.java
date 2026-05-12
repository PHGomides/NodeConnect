package main.java.com.nodeconnect.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        System.out.println("Iniciando o servidor na porta " + port);

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Servidor pronto! Aguardando conexão...");

            while (true) {

                Socket client = server.accept();
                System.out.println("Novo cliente conectado: " + client.getInetAddress());
                Thread.startVirtualThread(() -> handleClient(client));
            }
        } catch (IOException e) {
            System.out.println("Erro crítico no servidor: " + e.getMessage());
        }
    }

    private void handleClient(Socket client) {
        try (
                DataInputStream in = new DataInputStream(client.getInputStream());
                DataOutputStream out = new DataOutputStream(client.getOutputStream())
        ) {
            while (true) {

                int length = in.readInt();

                if (length > 0) {

                    byte[] messageBytes = new byte[length];
                    in.readFully(messageBytes, 0, length);

                    String message = new String(messageBytes);
                    System.out.println("[Cliente " + client.getInetAddress() + "]: " + message);
                }
            }
        } catch (IOException e) {
            System.out.println("Conexão com cliente encerrada.");
        }
    }
}