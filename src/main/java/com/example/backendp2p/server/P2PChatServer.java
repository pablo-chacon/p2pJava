package com.example.backendp2p.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class P2PChatServer {


    private static final int PORT = 8181;


    public void start() throws IOException {
        //Starts the P2P chat server.
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("P2P Chat Server started on port " + PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

            Thread clientThread = new Thread(new ClientHandler(clientSocket));
            clientThread.start();
        }
    }

    // Handles single session client.
    private static class ClientHandler implements Runnable {

        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String clientMessage;
                while ((clientMessage = in.readLine()) != null) {
                    System.out.println("Received message from " +
                            clientSocket.getInetAddress().getHostAddress() + ": " + clientMessage);

                    // Broadcast the message to all connected clients
                    broadcastMessage(clientMessage);
                }
            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client connection: " + e.getMessage());
                }
            }
        }


        //Broadcasts a message to all connected clients.
        // @param message The message to broadcast.
        private void broadcastMessage(String message) {
            System.out.print(message);
        }

        public static void main(String[] args) {
            P2PChatServer chatServer = new P2PChatServer();
            try {
                chatServer.start();
            } catch (IOException e) {
                System.err.println("Error starting chat server: " + e.getMessage());

            }
        }
    }
}
