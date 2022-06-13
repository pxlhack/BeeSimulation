package com.simulation.EchoClient;

import com.simulation.Bee.BeePackage;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class EchoClient {
    private Socket clientSocket;

    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    BeePackage beePackage = new BeePackage();

    public void startConnection() {
        try {
            int port = 8000;
            String ip = "localhost";
            clientSocket = new Socket(ip, port);

            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ois = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public BeePackage getObj() {

        try {
            oos.writeUTF("GET.PACKAGE");
            oos.reset();

            beePackage = (BeePackage) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return beePackage;
    }

    public void sendObj(BeePackage beePackage) {
        try {
            oos.writeUTF("SEND.PACKAGE");
            oos.reset();

            oos.writeObject(beePackage);


            oos.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getClientsList() {
        String tmp = null;
        try {
            oos.writeUTF("GET.CLIENTS");
            oos.reset();
            tmp = ois.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tmp;
    }

    public void stopConnection() {
        try {
            ois.close();
            oos.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
