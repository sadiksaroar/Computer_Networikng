import java.io.*;
import java.net.*;

public class ServerThread {
    public static void main(String[] args) throws IOException {
        // Create a server socket on port 5000
        ServerSocket handshake = new ServerSocket(5000);
        System.out.println("Server connected at port: " + handshake.getLocalPort());
        System.out.println("Waiting for clients...\n");

        // Infinite loop to keep the server running and accepting clients
        while (true) {
            // Accept incoming client connections
            Socket com_socket = handshake.accept();
            System.out.println("A new client is connected: " + com_socket);

            // Create input and output streams for communication
            DataInputStream dis = new DataInputStream(com_socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(com_socket.getOutputStream());

            System.out.println("Assigning a new thread for this client...");

            // Create a new thread for the client
            Thread new_tunnel = new ClientHandler(com_socket, dis, dos);
            new_tunnel.start(); // Start the thread
        }
    }
}
