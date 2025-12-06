import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
import java.io.IOException;

public class QuoteUDPClient {

    // Server address and port
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        DatagramSocket clientSocket = null;
        Scanner scanner = null;

        try {
            // Resolve server address
            InetAddress serverAddress = InetAddress.getByName(SERVER_ADDRESS);

            // Create a DatagramSocket for sending and receiving packets
            clientSocket = new DatagramSocket();

            // Initialize scanner for user input
            scanner = new Scanner(System.in);

            // Buffers for sending and receiving data
            byte[] sendBuffer;
            byte[] receiveBuffer = new byte[1024];

            String clientMessage;

            // Infinite loop for communication
            while (true) {
                System.out.print("Enter GET for quote or exit to quit: ");
                clientMessage = scanner.nextLine();

                // Prepare the message for sending and convert to bytes
                sendBuffer = clientMessage.getBytes();

                // Create a DatagramPacket to send the message to the server
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, SERVER_PORT);

                // Send the packet to the server
                clientSocket.send(sendPacket);

                // Check if the client wants to terminate the connection
                if (clientMessage.equalsIgnoreCase("exit")) {
                    System.out.println("Client finished.");
                    break;
                }

                // Receive response from server
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                clientSocket.receive(receivePacket);

                // Extract and print the quote
                String serverResponse = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
                System.out.println("Quote received: " + serverResponse);
            }

        }

        catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }

        finally {
            // Close the socket and scanner in the finally
            clientSocket.close();
            scanner.close();
        }
    }
}