package com.simulation.Terminal;

import com.simulation.FileDataHandler.FileDataHandler;
import com.simulation.Habitat.Habitat;
import com.simulation.MainWindow.MainWindowController;

public class TerminalController {
    private static TerminalController instance;
    private static final TerminalView terminalView = TerminalView.getInstance();
    private static final MainWindowController controller = MainWindowController.getInstance();
    private static final FileDataHandler fileDataHandler = new FileDataHandler();
    private static final Habitat h = Habitat.getHabitat();


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

            case "load" -> printCommand(String.format("Loaded %d bees", fileDataHandler.load(controller.getDailyTime())));

            case "close" -> {
                hideTerminal();
                clearTerminal();
            }

            case "clear" -> clearTerminal();


            case "help" -> printCommand("""
                    _______________
                    list of command:
                    start (stop) - start (stop) simulation,
                    getlive (-m), (-w) - return count of live (male), (worker) bees,
                    save - save bees to file,
                    load - load bees from file,
                    close - close terminal,
                    clear - clear terminal
                    _______________
                    """);

            default -> printCommand("Unknown command \"" + command + "\" ");
        }

    }

    private void printCommand(String s) {
        terminalView.getTerminalArea().appendText(s + "\n");
    }
}

