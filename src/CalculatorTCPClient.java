import java.io.*;
import java.net.*;

public class CalculatorTCPClient {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 9090;

    public static void main(String[] args) throws IOException {
        // Declaration of resources outside the try block so we can close in finally if needed
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        // BufferedReader for the input
        BufferedReader buffin = null;

        try {
            // Create a connection socket to the server
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to the server at " + SERVER_ADDRESS + ":" + SERVER_PORT);

            // Set up writer and reader for server communication
            out = new PrintWriter(socket.getOutputStream(), true); // true for auto-flush
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Set up reader for user input from the keyboard
            buffin = new BufferedReader(new InputStreamReader(System.in));

            String userExpression;

            // Communication loop - Read user input and send requests
            System.out.print("Enter expression (num op num) or 'exit' to quit: ");
            while ((userExpression = buffin.readLine()) != null) {
                // if the client wants to end the communication
                if (userExpression.equalsIgnoreCase("exit")) {
                    out.println("exit"); // Send "exit" to the server
                    break;
                }

                // Send the expression to the server
                out.println(userExpression);

                // Wait for and receive the response from the server
                String serverResponse = in.readLine();

                // Print the expression and result in the required format
                System.out.println(userExpression + " = " + serverResponse);

                System.out.print("Enter expression (num op num) or 'exit' to quit: ");
            }

            System.out.println("Client closed.");

        }

        // IO exception handle
        catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }

        // Other exceptions
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        finally {
            // Close all resources
            buffin.close();
            out.close();
            in.close();
            socket.close();
        }
    }
}