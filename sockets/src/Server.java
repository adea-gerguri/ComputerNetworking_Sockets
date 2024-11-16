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
                        try {
                            String key = "1234567890123456";
                            String decryptedMessage = AESDecryption.decrypt(argument, key);

                            response = "Message received: " + decryptedMessage;
                            System.out.println("Client message: " + decryptedMessage);
                        } catch (Exception e) {
                            response = "Error decrypting the message. ";
                            System.out.println("Decryption error: "+e.getMessage());
                        }
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

    private static String handleRead(String filename) {
        File file = new File(SHARED_FOLDER, filename);
        if (!file.exists()) return "File not found.";

        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            return "Content of " + filename + ": " + content;
        } catch (IOException e) {
            return "Error reading file.";
        }
    }

    private static String handleWrite(String filename, String content) {
        File file = new File(SHARED_FOLDER, filename);
        try {
            Files.write(file.toPath(), content.getBytes());
            return "File " + filename + " written successfully.";
        } catch (IOException e) {
            return "Error writing to file.";
        }
    }

    private static String handleCreate(String filename) {
        File file = new File(SHARED_FOLDER, filename);
        try {
            if (file.createNewFile()) {
                return "File " + filename + " created successfully.";
            } else {
                return "File " + filename + " already exists.";
            }
        } catch (IOException e) {
            return "Error creating file.";
        }
    }

    private static String handleDelete(String filename) {
        File file = new File(SHARED_FOLDER, filename);
        if (!file.exists()) return "File not found.";

        if (file.delete()) {
            return "File " + filename + " deleted successfully.";
        } else {
            return "Error deleting file.";
        }
    }

    private static String handleCreateFolder(String folderName) {
        File folder = new File(SHARED_FOLDER, folderName);
        if (folder.exists()) return "Folder already exists.";

        if (folder.mkdirs()) {
            return "Folder " + folderName + " created successfully.";
        } else {
            return "Error creating folder.";
        }
    }

    private static String handleDeleteFolder(String folderName) {
        File folder = new File(SHARED_FOLDER, folderName);
        if (!folder.exists()) return "Folder not found.";

        if (folder.isDirectory() && folder.delete()) {
            return "Folder " + folderName + " deleted successfully.";
        } else {
            return "Error deleting folder.";
        }
    }
}
