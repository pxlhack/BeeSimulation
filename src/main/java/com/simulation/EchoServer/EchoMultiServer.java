package com.simulation.EchoServer;

import com.simulation.Bee.BeePackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class EchoMultiServer {

    private static EchoMultiServer instance;
    private static final Vector<EchoClientHandler> clientList = new Vector<>();
    private static int id = 0;

    public BeePackage beePackage;
    public BeePackage bufferPackage;


    public static synchronized EchoMultiServer getInstance() {
        if (instance == null) {
            instance = new EchoMultiServer();
        }
        return instance;
    }

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        int port = 8000;
        try {
            System.out.println("Server runned...");
            serverSocket = new ServerSocket(port);
            while (true) {
                EchoClientHandler ech = new EchoClientHandler(serverSocket.accept(), id++);
                clientList.add(ech);

                ech.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert serverSocket != null;
            stop(serverSocket);
        }
    }

    public static String getClients() {
        StringBuilder names = new StringBuilder();
        for (EchoClientHandler client : clientList)
            names.append("Client ").append(client.getClientId()).append("\n");
        return names.toString();
    }

    public static void stop(ServerSocket serverSocket) {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static class EchoClientHandler extends Thread {
        private final Socket clientSocket;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;
        private final int id;
        EchoMultiServer ems = EchoMultiServer.getInstance();

        public EchoClientHandler(Socket socket, int id) {
            this.id = id;
            this.clientSocket = socket;
        }

        public void run() {
            try {
                oos = new ObjectOutputStream(clientSocket.getOutputStream());
                ois = new ObjectInputStream(clientSocket.getInputStream());

                while (!clientSocket.isClosed() && !clientSocket.isOutputShutdown() && !clientSocket.isInputShutdown()) {
                    String request = ois.readUTF();
                    switch (request) {
                        case "SEND.PACKAGE" -> getObj();
                        case "GET.PACKAGE" -> sendObj();
                        case "GET.CLIENTS" -> sendClientsList();
                    }
                }
                oos.close();
                ois.close();
                clientSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void getObj() {
            try {
                System.out.println("get props");
                ems.beePackage = (BeePackage) ois.readObject();

                ems.bufferPackage = ems.beePackage;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

        public void sendObj() {
            try {
                oos.writeObject(ems.bufferPackage);
                oos.reset();
                if (ems.bufferPackage.getSerializableBeeArrayList().isEmpty()) {
                    System.out.println("Buffer is empty");
                } else {
                    ems.bufferPackage.getSerializableBeeArrayList().clear();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendClientsList() {
            System.out.println("sending list of on-line clients");
            try {
                oos.writeUTF(getClients());
                oos.reset();

                System.out.println(getClients());
                System.out.println(clientList.size());


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        protected int getClientId() {
            return id;
        }

    }
}