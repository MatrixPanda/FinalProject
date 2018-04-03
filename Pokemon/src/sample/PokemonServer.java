package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class PokemonServer extends Thread {

    private ServerSocket server;
    private Socket connection;
    private BufferedReader in = null;
    // private int playerCount = 0;
    private static int newHpReceived = 0;

    private ArrayList<Integer> clientArray = new ArrayList<>();
    private static int clientCount = 0;
    private PokemonServer[] PokeThreads    =null;
    private DataInputStream dis;
    private DataOutputStream dos;



    public PokemonServer(int port) {
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        PokemonServer ps = new PokemonServer(8888);
        ps.start();
    }

    public void run() {
        try {
            /*
            if (clientCount < 2) {
                waitForConnection();
                // connection = server.accept();
                setUpStreams();
                clientCount++;
                notifyPlayerFound("no");
            } else {
                notifyPlayerFound("yes");
            }
            */

            waitForConnection();
            setUpStreams();
            while (true) {
                //  PokeThreads[clientCount] = new PokemonServer(8888);
                //  PokeThreads [clientCount].start();
                //   clientCount++;

                // System.out.println(" Waiting for someone to connect... \n");
                //  client = server.accept();
                //  System.out.println(" Now connected to " + client.getInetAddress().getHostName());
                //  client = server.accept();
                System.out.println("Players connected");  // it crashes tho if one player dc
                // closeConnection();

                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String clientSelection = in.readLine();

                StringTokenizer st = new StringTokenizer(clientSelection);
                String command = st.nextToken();  // First word

                if (command.equalsIgnoreCase("ATTACK")) {
                    receiveHp(connection);
                } else if (command.equalsIgnoreCase("UPDATEhp")) {
                    sendHpAway(newHpReceived);
                }

            }
        } catch (IOException e) {
            System.err.println("Someone Disconnected.");
            System.exit(0);
        }
    }


    // Wait for connection
    private void waitForConnection() throws IOException {
        System.out.println(" #Waiting for someone to connect... \n");
        connection = server.accept();
        // clientCount++;
        System.out.println(" #Now connected to client ");
        // System.out.println(" Now connected to " + client.getInetAddress().getHostName());
        // connection = new Socket(InetAddress.getByName(serverIP), 8888);
    }


    private void setUpStreams() throws IOException{
        dos = new DataOutputStream(connection.getOutputStream());
        dos.flush();

        dis = new DataInputStream(connection.getInputStream());

        System.out.println("Streams are now setup \n");
    }


    // Close paths and connection
    public void closeConnection() {
        System.out.println("\n Closing Connections... \n");
        try {
            //  output.close(); //path to client closes
            //  input.close(); //client's input path to server closes
            connection.close(); //client server connection closes
        } catch (IOException e) {
            e.printStackTrace();
            //  System.exit(0);
        }
    }


    private void receiveHp(Socket clientSock) {
        try {
            dis = new DataInputStream(clientSock.getInputStream());

            String temp = dis.readUTF();
            newHpReceived = Integer.valueOf(temp);
            System.out.println("Received HP: " + newHpReceived);

            dis.close();
        } catch (IOException e) {
            System.err.println("Error reading from client.");
        }
    }


    private void sendHpAway(int hpValue) {
        try {
            dos = new DataOutputStream(connection.getOutputStream());

            dos.writeUTF(String.valueOf(hpValue));

            dos.close();
        } catch (IOException e) {
            System.err.println("Error reading from socket.");
        }
    }


    private void notifyPlayerFound(String found) {
        try {
            dos = new DataOutputStream(connection.getOutputStream());

            dos.writeUTF(found);

            dos.close();
        } catch (IOException e) {
            System.err.println("Error sending to client.");
        }
    }

}
