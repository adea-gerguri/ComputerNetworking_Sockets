import java.io.*;
import java.net.*;
import java.nio.file.*;

public class Server {
    private static final int PORT = 6000;
    private static final String IP_ADDRESS = "172.20.10.2";
    private static final String SHARED_FOLDER = "sockets/sharedFolder";
    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(PORT, InetAddress.getByName(IP_ADDRESS));
            System.out.println("Server is listening on " + IP_ADDRESS + ":" + PORT);

            Files.createDirectories(Paths.get(SHARED_FOLDER));

            byte[] receiveBuffer = new byte[1024];
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                serverSocket.receive(receivePacket);

                String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                System.out.println("Received: " + clientMessage + " from: " + clientAddress + ":" + clientPort);

                String response;
                String[] parts = clientMessage.split(" ", 2);
                String command = parts[0];
                String argument = parts.length > 1 ? parts[1] : "";

                switch (command.toUpperCase()) {
                    case "SENDMSG":
                        response = "Message received: " + argument;
                        System.out.println("Client message: " + argument);
                        break;

                    case "READ":
                        response = handleRead(argument);
                        break;

                    case "WRITE":
                        String[] writeParts = argument.split(" ", 2);
                        if (writeParts.length < 2) {
                            response = "Error: Please specify file name and content.";
                        } else {
                            String filename = writeParts[0];
                            String content = writeParts[1];
                            response = handleWrite(filename, content);
                        }
                        break;

                    case "CREATE":
                        response = handleCreate(argument);
                        break;

                    case "DELETE":
                        response = handleDelete(argument);
                        break;

                    case "CREATEFOLDER":
                        response = handleCreateFolder(argument);
                        break;

                    case "DELETEFOLDER":
                        response = handleDeleteFolder(argument);
                        break;
                    case "EXECUTEFILE":
                        response = handleExecuteFile(argument);
                        break;
                    default:
                        response = "Unknown command.";
                }

                byte[] sendBuffer = response.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String handleExecuteFile(String filename) {
        File file = new File(SHARED_FOLDER, filename);
        if (!file.exists()) {
            return "File not found";
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("notepad.exe", file.getAbsolutePath());
            processBuilder.start();
            return "File opened successfully in Notepad";
        } catch (IOException e) {
            System.out.println("Error: " + e);
            return "Error executing file";
        }
    }
    //metodat...

}