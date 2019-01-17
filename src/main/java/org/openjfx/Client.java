package org.openjfx;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Client class.
 *
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

    // Communicate with the server
    public String processRequest(String request) {
        try {
            // set up connection with the MQTT.
            client = new Socket(InetAddress.getLocalHost(), 49975);
            // set up a Buffer for the reading from server
            inFromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
            // set up a Buffer for the writing to server
            outToServer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            // send the request
            outToServer.write(request);
            // create a newline
            outToServer.newLine();
            // flush the data
            outToServer.flush();
            // read data from the server
            strServer = inFromServer.readLine();
            // read the data from the server
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

