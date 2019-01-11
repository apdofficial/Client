package org.openjfx;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * client class.
 * <p>
 * Just simple org.openjfx.Client connecting to the Server.
 *
 * @author Group 3
 * @version 0.1
 */
public class Client {
    private Socket client = null;
    private BufferedReader inFromServer = null;
    private BufferedWriter outToServer = null;
    private String strServer;

    public Client() {
    }

    public String processRequest(String request) {
        try {
            client = new Socket(InetAddress.getLocalHost(), 49975);
            inFromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
            outToServer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            outToServer.write(request);
            outToServer.newLine();
            outToServer.flush();
            strServer = inFromServer.readLine();
            System.out.println("Server: " + strServer);
            System.out.print("\n");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert inFromServer != null;
                inFromServer.close();
                assert outToServer != null;
                outToServer.close();
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return strServer;
    }
}

