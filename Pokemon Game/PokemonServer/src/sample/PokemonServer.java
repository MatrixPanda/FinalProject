package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class PokemonServer extends Thread {

    private ServerSocket server;
    private Socket connection;
    private BufferedReader in = null;
    // private int playerCount = 0;
    private static int score = 0;
    private DataInputStream dis;
    private DataOutputStream dos;


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
                connection = server.accept();
                System.out.println("Player connected");

                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String clientSelection = in.readLine();

                StringTokenizer st = new StringTokenizer(clientSelection);
                String command = st.nextToken();  // First word

                if (command.equalsIgnoreCase("SEND")) {
                    receiveScore(connection);
                } else if (command.equalsIgnoreCase("VIEW")) {
                    processScoreView(score);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void receiveScore(Socket connection) {
        try {
            DataInputStream dis = new DataInputStream(connection.getInputStream());

            String temp = dis.readUTF();
           // score = Integer.valueOf(temp);
            System.out.println("Msg received -> " + temp);

            // Write score to file, file name is date and time
            Date d = new Date();
            String fileName = String.format(String.valueOf(d));
            System.out.println(fileName);
            fileName = (fileName.substring(0,13)) + "h " + (fileName.substring(14,16)) + "m " +
                        (fileName.substring(17,19)) + "s " + (fileName.substring(20));

            FileWriter fileWriter = new FileWriter(fileName + ".txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.printf(temp);

            printWriter.close();
            dis.close();
        } catch (IOException e) {
            System.err.println("Client Disconnected.");
        }
    }


    private void processScoreView(int value) {
        try {
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());

            dos.writeUTF(String.valueOf(value));

            dos.close();
        } catch (IOException e) {
            System.err.println("Client Disconnected.");
        }
    }

}
