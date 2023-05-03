package com.simulation.Terminal;

import com.simulation.Bee.Bee;
import com.simulation.Bee.BeePackage;
import com.simulation.Bee.SerializableBee;
import com.simulation.Bee.WorkerBee;
import com.simulation.DataBaseHandler.DataBaseHandler;
import com.simulation.EchoClient.EchoClient;
import com.simulation.FileDataHandler.FileDataHandler;
import com.simulation.Model.Habitat;
import com.simulation.MainWindow.MainWindowController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class TerminalController {
    private static TerminalController instance;
    private static final TerminalView terminalView = TerminalView.getInstance();
    private static final MainWindowController controller = MainWindowController.getInstance();
    private static final FileDataHandler fileDataHandler = new FileDataHandler();
    private static final Habitat h = Habitat.getHabitat();

    private static final EchoClient ech = new EchoClient();


    public static synchronized TerminalController getInstance() {
        if (instance == null) {
            instance = new TerminalController();
        }
        return instance;
    }

    private TerminalController() {
    }

    public void startTerminal() {
        terminalView.getStage().show();
        terminalView.getInputTextField().setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case ESCAPE -> hideTerminal();
                case ENTER -> enterCommand();
            }
        });
    }

    public void clearTerminal() {
        terminalView.getTerminalArea().setText("");
        terminalView.getInputTextField().setText("");
    }

    private void clearInput() {
        terminalView.getInputTextField().setText("");
    }

    public void hideTerminal() {
        terminalView.getStage().hide();
    }

    public void enterCommand() {

        String command = terminalView.getInputTextField().getText();
        System.out.println(command);
        clearInput();
        switch (command) {
            case "start" -> {
                controller.startSim();
                printCommand("Simulation started...");
            }
            case "stop" -> {
                controller.stopSim();
                printCommand("Simulation stopped...");
            }
            case "getlive -m" -> printCommand(String.format("Count of live Male Bees: %d", h.getMaleCount()));
            case "getlive -w" -> printCommand(String.format("Count of live Worker Bees: %d", h.getWorkerCount()));
            case "getlive" -> printCommand(String.format("Count of live Bees: %d", h.getWorkerCount() + h.getMaleCount()));

            case "save" -> printCommand(String.format("Saved %d bees", fileDataHandler.save()));

            case "load" -> printCommand(String.format("Loaded %d bees", fileDataHandler.load(controller.getCurrentTime())));

            case "connect" -> {
                ech.startConnection();
                printCommand("You connected\n" +
                        "Current list of clients:\n" +
                        ech.getClientsList());
            }

            case "disconnect" -> {
                ech.stopConnection();
                printCommand("You disconnected");
            }

            case "sendbees" -> {
                int size = (int) (Math.random() * (h.getBeeArray().size() - 2) + 1);
                ArrayList<SerializableBee> serializableBeeArrayList = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    Bee bee = h.getBeeArray().get(0);
                    serializableBeeArrayList.add(new SerializableBee(bee, h.getTreeMap().get(bee.getId())));
                    h.killBeeFromCollection(0);
                }

                BeePackage beePackage = new BeePackage();
                beePackage.setSerializableBeeArrayList(serializableBeeArrayList);
                ech.sendObj(beePackage);
                printCommand(String.format("Sent %d bees", size));
            }

            case "getbees" -> {
                BeePackage beePackage = ech.getObj();

                ArrayList<SerializableBee> serializableBeeArrayList = new ArrayList<>(beePackage.getSerializableBeeArrayList());

                if (!serializableBeeArrayList.isEmpty()) {

                    for (SerializableBee sb : serializableBeeArrayList) {
                        Bee bee = sb.getBee();
                        int[] c = h.getRandomCoordinates();

                        ImageView imageView;

                        if (bee instanceof WorkerBee) {
                            imageView = new ImageView(new Image("C:\\image\\w_bee.png"));
                            h.increaseWorkerCount();
                        } else {
                            imageView = new ImageView(new Image("C:\\image\\m_bee.png"));
                            h.increaseMaleCount();
                        }

                        bee.setImageView(imageView);
                        h.addToCollections(controller.getCurrentTime(), bee, c);
                    }
                    printCommand(String.format("Got %d bees", serializableBeeArrayList.size()));
                } else {
                    printCommand("Buffer is empty");
                }

            }

            case "getclients" -> printCommand(ech.getClientsList());

            case "close" -> {
                hideTerminal();
                clearTerminal();
            }

            case "clear" -> clearTerminal();

            case "loadfromdb" -> {
                DataBaseHandler.selectMaleFromTable(controller.getCurrentTime());
                DataBaseHandler.selectWorkerFromTable(controller.getCurrentTime());
            }

            case "loadfromdb -m" -> DataBaseHandler.selectMaleFromTable(controller.getCurrentTime());

            case "loadfromdb -w" -> DataBaseHandler.selectWorkerFromTable(controller.getCurrentTime());

            case "savetodb" -> {
                DataBaseHandler.insertMaleToTable();
                DataBaseHandler.insertWorkerToTable();
            }

            case "savetodb -m" -> DataBaseHandler.insertMaleToTable();

            case "savetodb -w" -> DataBaseHandler.insertWorkerToTable();

            case "help" -> printCommand("""
                    _______________
                    list of command:
                    start (stop) - start (stop) simulation,
                    getlive (-m), (-w) - return count of live (male), (worker) bees,
                    save - save bees to file,
                    load - load bees from file,
                    close - close terminal,
                    clear - clear terminal,
                    connect - connect to server,
                    disconect - disconnect from server,
                    getclients - get a list of current clients,
                    sendbees - send a random сount of bees to  server,
                    getbees - get a random сount of bees from server
                    savetodb (-m), (-w)- save (male), (worker) bees to database
                    loadfromdb (-m), (-w) - load (male), (worker) bees from database
                    _______________
                    """);

            default -> printCommand("Unknown command \"" + command + "\" ");
        }

    }

    private void printCommand(String s) {
        terminalView.getTerminalArea().appendText(s + "\n");
    }
}

