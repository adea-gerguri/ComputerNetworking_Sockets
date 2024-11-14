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