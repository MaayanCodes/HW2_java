import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import java.io.IOException;

public class QuoteUDPServer {

    // The port required by the assignment
    private static final int PORT = 8080;

    // Array of quotes
    private static final String[] QUOTES = {
            "The best way to predict the future is to create it.",
            "Success is not final, failure is not fatal: it is the courage to continue that counts.",
            "The only way to do great work is to love what you do.",
            "Strive not to be a success, but rather to be of value.",
            "Life is what happens when you're busy making other plans.",
            "The mind is everything. What you think you become."
    };

    public static void main(String[] args) {
        DatagramSocket serverSocket = null;

        try {
            // Create a DatagramSocket bound to the specified port
            serverSocket = new DatagramSocket(PORT);
            System.out.println("UDP Quote Server listening on port " + PORT + "...");

            // Random var for quote selection
            Random rand = new Random();

            // Buffers for receiving and sending data
            byte[] receiveBuffer = new byte[1024];
            byte[] sendBuffer;

            // Infinite loop to keep the server running and listening
            while(true) {

                // Prepare a packet to receive incoming data
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                serverSocket.receive(receivePacket);

                // Extract the message from the received packet
                String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Request received: " + clientMessage);

                // Check if the client wants to terminate the connection
                if (clientMessage.equalsIgnoreCase("exit")) {
                    System.out.println("Server shutting down.");
                    break;
                }

                // Choose a random quote from the array
                int quoteIndex = rand.nextInt(QUOTES.length);
                String randomQuote = QUOTES[quoteIndex];

                // Prepare the response message and convert to bytes
                sendBuffer = randomQuote.getBytes();

                // Get the client's IP address and port from the received packet
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Create a packet with the response data, client address, and client port
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket); // Send the response packet to the client
            }

        }

        catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }

        finally {
            // Close the socket
            serverSocket.close();
            }
    }
}