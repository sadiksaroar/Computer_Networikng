import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

class ClientHandler extends Thread {
    // Date and Time formats
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");

    // Client communication socket and data streams
    final Socket com_tunnel;
    final DataInputStream dis_tunnel;
    final DataOutputStream dos_tunnel;

    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.com_tunnel = s;
        this.dis_tunnel = dis;
        this.dos_tunnel = dos;
    }

    @Override
    public void run() {
        String received = ""; // Message received from client
        String toreturn = ""; // Response to send to client

        while (true) {
            try {
                // Prompt the client for input
                dos_tunnel.writeUTF("What do you want? [Date/Time] (Type 'Exit' to disconnect)");
                received = dis_tunnel.readUTF(); // Read client input

                // Handle client request
                if (received.equalsIgnoreCase("Exit")) {
                    System.out.println("Client " + this.com_tunnel + " sent an exit request.");
                    System.out.println("Closing connection with client...");
                    this.com_tunnel.close();
                    System.out.println("Connection closed.");
                    break;
                }

                // Process request
                Date date = new Date();
                switch (received) {
                    case "Date":
                        toreturn = fordate.format(date);
                        dos_tunnel.writeUTF(toreturn);
                        break;
                    case "Time":
                        toreturn = fortime.format(date);
                        dos_tunnel.writeUTF(toreturn);
                        break;
                    default:
                        dos_tunnel.writeUTF("Invalid input. Please type 'Date' or 'Time'.");
                        break;
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }

        try {
            // Close streams and socket
            this.dis_tunnel.close();
            this.dos_tunnel.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
