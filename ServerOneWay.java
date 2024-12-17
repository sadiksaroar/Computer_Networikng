import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerOneWay {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(5000); // Create a server socket on port 5000
        System.out.println("Server is connected at port no: " + ss.getLocalPort());
        System.out.println("Waiting for the client...");

        Socket s = ss.accept(); // Accept client connection
        System.out.println("Client request is accepted at port no: " + s.getPort());
        System.out.println("Server’s Communication Port: " + s.getLocalPort());

        DataInputStream input = new DataInputStream(s.getInputStream());
        String str = "";

        while (!str.equals("stop")) {
            str = input.readUTF(); // Read messages from client
            System.out.println("Client Says: " + str);
        }

        input.close(); // Close the input stream
        s.close(); // Close the socket
        ss.close(); // Close the server socket
        System.out.println("Server is closed.");
    }
}
