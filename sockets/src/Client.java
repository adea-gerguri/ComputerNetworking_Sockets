import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final int SERVER_PORT = 6000;
    private static final String SERVER_IP = "192.168.2.2";
    private static final String PASSKEY = "123";
    private static boolean IS_ADMIN = false;

    public static void main(String[] args) {
        try (DatagramSocket clientSocket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {

            InetAddress serverAddress = InetAddress.getByName(SERVER_IP);
            byte[] buffer = new byte[1024];

            System.out.println("Enter password:");
            String password = scanner.nextLine();

            if (password.equals(PASSKEY)) {
                IS_ADMIN = true;
                System.out.println("Admin privileges");
            } else {
                System.out.println("Only read privileges");
            }

            while (true) {
                System.out.println("Enter command: ");
                String command = scanner.nextLine();

                String name = "";
                if (!command.equalsIgnoreCase("sendmsg")) {
                    System.out.println("Enter file/folder name:");
                    name = scanner.nextLine();
                }

                switch (command.toLowerCase()) {
                    case "sendmsg":
                        System.out.println("Enter message to send:");
                        String message = scanner.nextLine();
                        sendCommand("SENDMSG " + message, clientSocket, serverAddress);
                        break;

                    case "readfile":
                        sendCommand("READ " + name, clientSocket, serverAddress);
                        break;

                    case "writefile":
                        if (IS_ADMIN) {
                            System.out.println("Enter content to write in the file:");
                            String content = scanner.nextLine();
                            sendCommand("WRITE " + name + " " + content, clientSocket, serverAddress);
                        } else {
                            System.out.println("Denied. You do not have write privileges.");
                        }
                        break;

                    case "createfile":
                        if (IS_ADMIN) {
                            sendCommand("CREATE " + name, clientSocket, serverAddress);
                        } else {
                            System.out.println("Denied. You do not have create privileges.");
                        }
                        break;

                    case "deletefile":
                        if (IS_ADMIN) {
                            sendCommand("DELETE " + name, clientSocket, serverAddress);
                        } else {
                            System.out.println("Denied. You do not have delete privileges.");
                        }
                        break;


                    case "createfolder":
                        if (IS_ADMIN) {
                            sendCommand("CREATEFOLDER " + name, clientSocket, serverAddress);
                        } else {
                            System.out.println("Denied. You do not have create folder privileges.");
                        }
                        break;

                    case "deletefolder":
                        if (IS_ADMIN) {
                            sendCommand("DELETEFOLDER " + name, clientSocket, serverAddress);
                        } else {
                            System.out.println("Denied. You do not have delete folder privileges.");
                        }
                        break;
                    case "execute":
                        if(IS_ADMIN){
                            sendCommand("EXECUTEFILE "+name, clientSocket, serverAddress);
                        }else{
                            System.out.println("Denied. You do not have execute privileges");
                        }

                    default:
                        System.out.println("Unknown command. Please try again.");
                }

                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                clientSocket.receive(receivePacket);
                String response = new String(receivePacket.getData(), 0, receivePacket.getLength());

                System.out.println("Server response: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendCommand(String command, DatagramSocket clientSocket, InetAddress serverAddress) throws IOException {
        byte[] sendBuffer = command.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, SERVER_PORT);
        clientSocket.send(sendPacket);
    }}
                }
