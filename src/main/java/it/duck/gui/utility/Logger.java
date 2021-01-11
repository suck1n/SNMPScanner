package it.duck.gui.utility;

import it.duck.gui.GUI;
import it.duck.utility.ThreadHandler;
import javafx.application.Platform;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TimerTask;

public class Logger {

    private final Queue<String> queue;

    public Logger() {
        queue = new PriorityQueue<>();

        ThreadHandler.registerNewTimer(new TimerTask() {
            @Override
            public void run() {
                printMessages();
            }
        }, 0, 500);
    }


    private void printMessages() {
        StringBuilder message = new StringBuilder();

        synchronized (queue) {
            while (!queue.isEmpty()) {
                message.append(queue.poll());
            }
        }

        if(message.length() > 0) {
            Platform.runLater(() -> GUI.getInstance().getConsole().appendText(message.toString()));
        }
    }

    private void log(String message) {
        synchronized (queue) {
            queue.add(message + "\n");
        }
    }

    public void info(String message) {
        log("[INFO][" + getDateAsString() + "] " + message);
    }

    public void warning(String message) {
        log("[WARNUNG][" + getDateAsString() + "] " + message);
    }

    public void error(String message) {
        log("[FEHLER][" + getDateAsString() + "] " + message);
    }

    private String getDateAsString() {
        LocalDateTime dateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM HH:mm:ss");

        return dateTime.format(formatter);
    }
}
