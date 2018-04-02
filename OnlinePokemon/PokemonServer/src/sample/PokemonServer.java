package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class PokemonServer extends Thread {

    private ServerSocket server;
    private Socket client;
    private BufferedReader in = null;
    // private int playerCount = 0;
    private static int newHpReceived = 0;


    public PokemonServer(int port) {
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Player connected");

                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String clientSelection = in.readLine();

                StringTokenizer st = new StringTokenizer(clientSelection);
                String command = st.nextToken();  // First word

                if (command.equalsIgnoreCase("ATTACK")) {
                    receiveHp(client);
                } else if (command.equalsIgnoreCase("UPDATEhp")) {
                    sendHpAway(newHpReceived);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void receiveHp(Socket clientSock) {
        try {
            DataInputStream dis = new DataInputStream(clientSock.getInputStream());

            String temp = dis.readUTF();
            newHpReceived = Integer.valueOf(temp);
            System.out.println("Updated HP: " + newHpReceived);

            dis.close();
        } catch (IOException e) {
            System.err.println("Error reading from client.");
        }
    }


    private void sendHpAway(int hpValue) {
        try {
            DataOutputStream dos = new DataOutputStream(client.getOutputStream());

            dos.writeUTF(String.valueOf(hpValue));

            dos.close();
        } catch (IOException e) {
            System.err.println("Error reading from socket.");
        }
    }

}
