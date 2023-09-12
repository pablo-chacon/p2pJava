package com.example.backendp2p.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


//Simple peer-to-peer chat client. Send and receive messages.
public class P2PChatClient {


    private String serverAddress;
    private int serverPort;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    /*
     * Constructs client with server address and port.
     * @param serverAddress.
     * @param serverPort.
     */
    public P2PChatClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void connect() throws IOException {
        //Connect to server
        socket = new Socket(serverAddress, serverPort);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    public String receiveMessage() throws IOException {
        return reader.readLine();
    }

    public void disconnect() throws IOException {
        socket.close();
    }

    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 12345;

        P2PChatClient client = new P2PChatClient(serverAddress, serverPort);

        try {
            // Connect to the chat server
            client.connect();

            // Start the chat loop
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            String input;

            while (true) {
                // Read user input from the console
                input = consoleReader.readLine();

                // Send the message to the chat server
                client.sendMessage(input);

                // Receive and display the response from the chat server
                String response = client.receiveMessage();
                System.out.println("Server: " + response);

                // Check if the user wants to exit the chat
                if (input.equalsIgnoreCase("exit")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Disconnect from the chat server
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}