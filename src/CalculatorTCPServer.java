import java.io.*;
import java.net.*;

public class CalculatorTCPServer {

    private static final int PORT = 9090; // The port required by the assignment

    public static void main(String[] args) throws IOException {
        // Declaration of resources outside the try block so we can close in finally if needed
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            // Create the server socket to listen for connections
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is listening on port " + PORT + "...");

            // Wait for a client connection
            clientSocket = serverSocket.accept();
            System.out.println("Client connected.");

            // Set up the writer and reader for string communication with the client
            out = new PrintWriter(clientSocket.getOutputStream(), true); // true for auto-flush
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String clientExpression;

            // Communication loop - Read expressions from the client until "exit" is received
            while ((clientExpression = in.readLine()) != null) {
                // Check if the client requested to close the connection
                if (clientExpression.equalsIgnoreCase("exit")) {
                    System.out.println("Client requested to close connection.");
                    break;
                }

                System.out.println("Received expression: " + clientExpression);

                // Perform the calculation and error handling
                String result = calculate(clientExpression);

                // Send the result back to the client
                out.println(result);
            }

        }

        catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }

        finally {
            // Close all resources to release the port and connections
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        }
    }

    /**
     * Parses and performs the arithmetic calculation.
     * Format: "num operator num" with a single space between each part.
     * Supports: +, -, *, /
     * @param expression - The arithmetic string to calculate.
     * @return The result as a string, or an error message if invalid.
     */
    private static String calculate(String expression) {
        // Split the string by spaces
        String[] parts = expression.trim().split(" ");

        // Format check: Must have exactly 3 parts (num1, operator, num2
        if (parts.length != 3) {
            return "Error: Invalid expression";
        }

        try {
            // try to convert the string to numbers
            double num1 = Double.parseDouble(parts[0]);
            String operator = parts[1];
            double num2 = Double.parseDouble(parts[2]);
            // set result for 0
            double result = 0;

            // Perform the calculation
            switch (operator) {
                case "+": result = num1 + num2;
                break;

                case "-": result = num1 - num2;
                break;

                case "*": result = num1 * num2;
                break;

                case "/":
                    // Error handling - Division by zero
                    if (num2 == 0) {
                        return "Error: Cant do division by zero";
                    }

                    result = num1 / num2;
                    break;

                default:
                    // Error handling - Invalid operator
                    return "Error: Invalid expression";
            }

            // Return the result as a string
            return String.valueOf(result);

        }
        catch (NumberFormatException e) {
            // Error handling - One of the parts is not a valid number
            return "Error: Invalid expression";
        }
    }
}